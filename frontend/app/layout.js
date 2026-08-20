import './globals.css';

export const metadata = {
  title: 'MindBrief AI Summarizer',
  description: 'AI web summarizer dashboard',
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
