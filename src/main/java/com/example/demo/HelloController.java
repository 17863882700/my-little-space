
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(value = "/", produces = "text/html;charset=UTF-8")
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="zh-CN">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>我的小宇宙</title>
              <style>
                * { box-sizing: border-box; }
                body {
                  margin: 0; min-height: 100vh; color: #fff;
                  font-family: "Microsoft YaHei", sans-serif;
                  background: radial-gradient(ellipse at top, #39305e, #17182d 65%, #10101e);
                }
                header { text-align: center; padding: 34px 15px 20px; }
                header small { color: #d8b4fe; letter-spacing: 4px; }
                h1 { margin: 12px 0; font-size: clamp(26px,5vw,40px); }
                header p { color: #c4b5fd; font-size: 14px; }
                main {
                  max-width: 900px; margin: auto; padding: 12px;
                  display: grid; grid-template-columns: repeat(2,minmax(0,1fr)); gap: 16px;
                }
                .card {
                  padding: 22px; border: 1px solid #ffffff17;
                  border-radius: 20px; background: #ffffff0b;
                  box-shadow: 0 12px 35px #0002; min-width: 0;
                }
                .wide { grid-column: 1 / -1; }
                h2 { margin: 0 0 18px; font-size: 19px; }
                .emoji { font-size: 32px; }
                label { display: block; color: #d8d4f4; font-size: 13px; margin: 14px 0 7px; }
                input, button {
                  font: inherit; border-radius: 10px; padding: 11px;
                }
                input {
                  width: 100%; color: white; background: #17152c;
                  border: 1px solid #655784; color-scheme: dark;
                }
                button {
                  border: 0; background: #a78bfa; color: #21173e;
                  font-weight: bold; cursor: pointer; margin-top: 12px;
                }
                button:hover { filter: brightness(1.1); }
                .number { font-size: clamp(30px,6vw,46px); color: #f0abfc; font-weight: bold; margin: 16px 0 5px; }
                .muted { color: #b7b2d4; font-size: 13px; line-height: 1.7; }
                .row { display: flex; gap: 10px; align-items: center; flex-wrap: wrap; }
                .row button { flex: 1; }
                audio { width: 100%; margin-top: 16px; }
                #status { margin-top: 12px; color: #f5d0fe; font-size: 14px; }
                footer { text-align: center; padding: 25px; color: #8d88ad; font-size: 12px; }
                @media(max-width:560px) {
                  main { grid-template-columns: 1fr; }
                  .wide { grid-column: auto; }
                  .card { padding: 19px; }
                }
              </style>
            </head>
            <body>
              <header>
                <small>MY LITTLE SPACE</small>
                <h1>把生活调成喜欢的频率 ✨</h1>
                <p>倒数期待的日子，也别忘记享受今天。</p>
              </header>

              <main>
                <section class="card">
                  <div class="emoji">🎂</div>
                  <h2>生日倒计时</h2>
                  <label for="birthday">选择你的生日</label>
                  <input type="date" id="birthday">
                  <div class="number" id="days">-- 天</div>
                  <div class="muted" id="birthdayText">选择生日，看看还有多久到特别的日子。</div>
                </section>

                <section class="card">
                  <div class="emoji">🎵</div>
                  <h2>我的音乐角落</h2>
                  <label for="musicFile">选择电脑或手机里的歌曲</label>
                  <input type="file" id="musicFile" accept="audio/*">
                  <div class="muted" id="musicName">选择音频文件后即可播放。</div>
                  <audio id="audio" controls></audio>
                  <button id="clearMusic">停止并清除歌曲</button>
                </section>

                <section class="card wide">
                  <div class="emoji">⏰</div>
                  <h2>专属闹钟</h2>
                  <p class="muted">设置你希望提醒的日期和时间。页面保持打开时，会在到点后播放提示音并弹出提醒。</p>
                  <label for="alarmTime">闹钟时间</label>
                  <input type="datetime-local" id="alarmTime">
                  <div class="row">
                    <button id="setAlarm">设置闹钟</button>
                    <button id="cancelAlarm">取消闹钟</button>
                  </div>
                  <div id="status" role="status">还没有设置闹钟。</div>
                </section>
              </main>
              <footer>为平凡的日子，留一点期待 ♡</footer>

              <script>
                const birthday = document.getElementById('birthday');
                const daysEl = document.getElementById('days');
                const birthdayText = document.getElementById('birthdayText');

                birthday.addEventListener('change', updateBirthday);

                function updateBirthday() {
                  if (!birthday.value) return;
                  const parts = birthday.value.split('-').map(Number);
                  const month = parts[1], day = parts[2];
                  const now = new Date();
                  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                  let year = now.getFullYear();
                  let next = new Date(year, month - 1, day);

                  while (next < today) {
                    year++;
                    next = new Date(year, month - 1, day);
                  }

                  const remaining = Math.round((next - today) / 86400000);
                  daysEl.textContent = remaining + ' 天';
                  birthdayText.textContent = remaining === 0
                    ? '生日快乐！今天是你的特别日子 🎉'
                    : '距离 ' + year + ' 年 ' + month + ' 月 ' + day + ' 日，还有 ' + remaining + ' 天。';
                }

                const musicFile = document.getElementById('musicFile');
                const audio = document.getElementById('audio');
                let musicUrl = null;

                musicFile.addEventListener('change', () => {
                  if (musicUrl) URL.revokeObjectURL(musicUrl);
                  const file = musicFile.files[0];
                  if (!file) return;
                  musicUrl = URL.createObjectURL(file);
                  audio.src = musicUrl;
                  document.getElementById('musicName').textContent = '当前歌曲：' + file.name;
                  audio.play().catch(() => {
                    document.getElementById('musicName').textContent =
                      '歌曲已载入，请点击播放器的播放按钮。';
                  });
                });

                document.getElementById('clearMusic').onclick = () => {
                  audio.pause();
                  audio.removeAttribute('src');
                  audio.load();
                  if (musicUrl) URL.revokeObjectURL(musicUrl);
                  musicUrl = null;
                  musicFile.value = '';
                  document.getElementById('musicName').textContent = '已清除歌曲。';
                };

                let alarmTimer = null;
                let alarmAt = 0;
                let alarmFired = false;
                let audioContext = null;

                function beep() {
                  try {
                    audioContext = audioContext || new (window.AudioContext || window.webkitAudioContext)();
                    audioContext.resume();
                    [0, 0.35, 0.7].forEach(delay => {
                      const osc = audioContext.createOscillator();
                      const gain = audioContext.createGain();
                      osc.type = 'sine';
                      osc.frequency.value = 880;
                      gain.gain.setValueAtTime(0.0001, audioContext.currentTime + delay);
                      gain.gain.exponentialRampToValueAtTime(0.25, audioContext.currentTime + delay + 0.03);
                      gain.gain.exponentialRampToValueAtTime(0.0001, audioContext.currentTime + delay + 0.22);
                      osc.connect(gain);
                      gain.connect(audioContext.destination);
                      osc.start(audioContext.currentTime + delay);
                      osc.stop(audioContext.currentTime + delay + 0.23);
                    });
                  } catch (e) {
                    console.log('浏览器未能播放提示音', e);
                  }
                }

                document.getElementById('setAlarm').onclick = () => {
                  const value = document.getElementById('alarmTime').value;
                  if (!value) {
                    document.getElementById('status').textContent = '请先选择闹钟日期和时间。';
                    return;
                  }
                  alarmAt = new Date(value).getTime();
                  if (!Number.isFinite(alarmAt) || alarmAt <= Date.now()) {
                    document.getElementById('status').textContent = '请选择未来的时间。';
                    return;
                  }
                  alarmFired = false;
                  clearInterval(alarmTimer);
                  document.getElementById('status').textContent =
                    '闹钟已设置：' + new Date(alarmAt).toLocaleString();
                  alarmTimer = setInterval(() => {
                    if (!alarmFired && Date.now() >= alarmAt) {
                      alarmFired = true;
                      clearInterval(alarmTimer);
                      beep();
                      document.getElementById('status').textContent = '⏰ 时间到！记得处理你的事情。';
                      alert('⏰ 闹钟响啦！');
                    }
                  }, 500);
                };

                document.getElementById('cancelAlarm').onclick = () => {
                  clearInterval(alarmTimer);
                  alarmTimer = null;
                  alarmFired = false;
                  document.getElementById('status').textContent = '闹钟已取消。';
                };

                document.addEventListener('visibilitychange', () => {
                  if (!document.hidden && alarmTimer && !alarmFired && Date.now() >= alarmAt) {
                    alarmFired = true;
                    clearInterval(alarmTimer);
                    beep();
                    document.getElementById('status').textContent = '⏰ 时间到！';
                    alert('⏰ 闹钟响啦！');
                  }
                });
              </script>
            </body>
            </html>
            """;
    }
}
