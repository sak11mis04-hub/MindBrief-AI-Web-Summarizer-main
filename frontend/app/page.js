'use client';

import { useMemo, useState } from 'react';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://127.0.0.1:5000';

export default function HomePage() {
  const [topic, setTopic] = useState('artificial intelligence');
  const [results, setResults] = useState([]);
  const [summary, setSummary] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const searchQuery = useMemo(() => topic.trim(), [topic]);

  async function handleSearch() {
    if (!searchQuery) {
      setError('Please enter a topic to search.');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const res = await fetch(`${API_URL}/api/search?query=${encodeURIComponent(searchQuery)}`);
      const data = await res.json();

      if (!res.ok) throw new Error(data.error || 'Search failed');

      setResults(data.results || []);
      if (data.results?.[0]) {
        const text = data.results.map((item) => `${item.title}\n${item.snippet}`).join('\n\n');
        const summaryRes = await fetch(`${API_URL}/api/summarize?topic=${encodeURIComponent(searchQuery)}&text=${encodeURIComponent(text)}`);
        const summaryData = await summaryRes.json();
        setSummary(summaryData.summary || '');
      }
    } catch (err) {
      setError(err.message || 'Something went wrong');
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="page-shell">
      <div className="app-card">
        <header className="header">
          <div>
            <p className="eyebrow">AI Research Assistant</p>
            <h1>MindBrief</h1>
          </div>
        </header>

        <div className="search-row">
          <input
            value={topic}
            onChange={(e) => setTopic(e.target.value)}
            placeholder="Search any topic..."
            aria-label="Search for a topic"
          />
          <button onClick={handleSearch} disabled={loading}>
            {loading ? 'Searching...' : 'Search'}
          </button>
        </div>

        {error && <p className="error">{error}</p>}

        <div className="content-grid">
          <section className="panel">
            <h2>Sources</h2>
            {results.length === 0 ? (
              <p className="empty">No results yet. Try searching for a topic.</p>
            ) : (
              <ul className="results">
                {results.map((item) => (
                  <li key={item.url}>
                    <a href={item.url} target="_blank" rel="noreferrer">{item.title}</a>
                    <p>{item.snippet}</p>
                  </li>
                ))}
              </ul>
            )}
          </section>

          <section className="panel summary-panel">
            <h2>Summary</h2>
            {summary ? (
              <pre>{summary}</pre>
            ) : (
              <p className="empty">Your AI summary will appear here.</p>
            )}
          </section>
        </div>
      </div>
    </main>
  );
}
