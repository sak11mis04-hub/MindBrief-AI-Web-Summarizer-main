const test = require('node:test');
const assert = require('node:assert/strict');
const { spawn } = require('node:child_process');

function wait(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function fetchJson(url) {
  const response = await fetch(url);
  return {
    status: response.status,
    body: await response.json()
  };
}

test('server exposes health and search endpoints', async () => {
  const child = spawn(process.execPath, ['server.js'], {
    cwd: __dirname + '/..',
    env: { ...process.env, PORT: '5001' },
    stdio: ['ignore', 'pipe', 'pipe']
  });

  try {
    await wait(1500);

    const health = await fetchJson('http://127.0.0.1:5001/api/health');
    assert.equal(health.status, 200);
    assert.equal(health.body.status, 'ok');

    const search = await fetchJson('http://127.0.0.1:5001/api/search?query=artificial%20intelligence');
    assert.equal(search.status, 200);
    assert.ok(Array.isArray(search.body.results));
    assert.ok(search.body.results.length > 0);
  } finally {
    child.kill('SIGTERM');
    await wait(300);
  }
});
