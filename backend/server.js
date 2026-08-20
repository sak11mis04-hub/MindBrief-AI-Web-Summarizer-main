const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');

dotenv.config();

const app = express();
const port = process.env.PORT || 5000;

app.use(cors({
  origin: process.env.FRONTEND_URL || 'http://localhost:3000',
  credentials: true,
}));
app.use(express.json());

const mockSearchResults = [
  {
    title: 'Introduction to AI',
    url: 'https://example.com/ai-intro',
    snippet: 'Artificial intelligence is the simulation of human intelligence in machines.'
  },
  {
    title: 'Machine Learning Overview',
    url: 'https://example.com/ml-overview',
    snippet: 'Machine learning is a subset of AI focused on learning patterns from data.'
  },
  {
    title: 'Deep Learning Basics',
    url: 'https://example.com/deep-learning',
    snippet: 'Deep learning uses neural networks to model complex patterns in data.'
  }
];

app.get('/api/health', (req, res) => {
  res.json({ status: 'ok', message: 'MindBrief backend is running' });
});

app.get('/api/search', (req, res) => {
  const query = (req.query.query || '').toString().trim();

  if (!query) {
    return res.status(400).json({ error: 'Missing query parameter' });
  }

  const results = mockSearchResults.map((item, index) => ({
    ...item,
    id: index + 1,
    score: 0.9 - index * 0.1,
    query,
  }));

  return res.json({ query, results });
});

app.get('/api/summarize', (req, res) => {
  const text = (req.query.text || '').toString();
  const topic = (req.query.topic || 'research topic').toString();

  if (!text) {
    return res.status(400).json({ error: 'Missing text parameter' });
  }

  const summary = [
    `# ${topic}`,
    '',
    '## Overview',
    `This summary is generated from the provided content about ${topic}.`,
    '',
    '## Key insights',
    '- The topic has multiple important dimensions.',
    '- Reliable sources and clear reasoning improve understanding.',
    '- Actionable conclusions are easier to form when the material is organized.',
    '',
    '## Final takeaway',
    'The main idea is to synthesize the available information into a concise, useful summary with clear structure.'
  ].join('\n');

  return res.json({ topic, summary });
});

app.listen(port, () => {
  console.log(`MindBrief backend listening on http://localhost:${port}`);
});
