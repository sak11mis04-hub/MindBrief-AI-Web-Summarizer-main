# MindBrief AI Summarizer

A modern full-stack web application that allows users to search the web for any topic, scrape content from multiple websites, and generate comprehensive AI-powered summaries using Google Gemini API. All summaries can be saved to a personal Vault and downloaded in multiple formats.

## 🎯 What is MindBrief?

MindBrief is an AI-powered research assistant that helps you quickly understand complex topics by:
- Searching the web for relevant sources
- Extracting and cleaning content from multiple websites
- Generating comprehensive summaries using Google Gemini AI
- Organizing your research in a personal vault
- Exporting summaries for offline use

## 🚀 Features

- **Web Search**: Search for any topic and discover relevant websites  
- **Content Scraping**: Extract content from multiple websites with intelligent parsing  
- **AI Summarization**: Generate comprehensive, single-page summaries from scraped content  
- **Download Summaries**: Download summaries as TXT or Markdown  
- **Vault Storage**: Save and manage your summaries in a personal vault  
- **Firebase Authentication**: Secure user authentication and data storage  
- **Modern UI**: Responsive interface with smooth animations  


## 📋 Prerequisites

- [Node.js](https://nodejs.org/) (v18 or higher)
- pnpm (recommended) or npm

> ⚠️ This project uses **pnpm** as the preferred package manager (see `pnpm-lock.yaml`).  
> You may use npm, but pnpm is recommended for consistent dependency versions.


## 🛠️ Quick Start

### 1. Install Dependencies

**Backend:**

```bash
cd backend
pnpm install
```

**Frontend:**

```bash
cd frontend
pnpm install
```

### 2. Start the Servers

You need to run **two servers** simultaneously:

**Terminal 1 - Backend:**

```bash
cd backend
pnpm run dev
```

**Terminal 2 - Frontend:**

```bash
cd frontend
pnpm run dev
```

### 3. Access the Application

Open your browser and navigate to: `http://localhost:3000`

## 📖 How to Use

1. **Search**: Enter a topic and click "Search" to find relevant websites
2. **Select Links**: Click on links in the sidebar to select them for summarization
3. **Generate Summary**: Click "Generate Summary" to create a comprehensive document
4. **Download**: Click the download button to save your summary as a file
5. **Save to Vault**: Save summaries for later access in your personal vault

## ⚙️ Configuration

### Step 1: Backend Environment Variables

Create a `.env` file in the `backend` folder:

```env
# Required for AI Summarization
GEMINI_API_KEY=your_gemini_api_key_here

# Optional: For persistent vault storage
# See Firebase Admin section below

# Optional: Server port (default: 5000)
PORT=5000

# Optional: Frontend URL for CORS (default: http://localhost:3000)
FRONTEND_URL=http://localhost:3000
```

**Getting a Gemini API Key:**
1. Go to [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Sign in with your Google account
3. Click "Create API Key"
4. Copy the key and add it to your `.env` file

**Note:** Without a Gemini API key, the system will still work and provide formatted summaries from scraped content (no AI enhancement).

### Step 2: Frontend Environment Variables

Create a `.env.local` file in the `frontend` folder:

```env

NEXT_PUBLIC_API_URL=http://127.0.0.1:5000
# Firebase Configuration (Required for Authentication)
NEXT_PUBLIC_FIREBASE_API_KEY=your_firebase_api_key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=your-project-id
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
NEXT_PUBLIC_FIREBASE_APP_ID=your_app_id
NEXT_PUBLIC_FIREBASE_MEASUREMENT_ID=your_measurement_id
```

**Setting up Firebase:**
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing one
3. Enable Authentication (Email/Password and Google)
4. Enable Firestore Database
5. Go to Project Settings > General
6. Scroll down to "Your apps" and add a web app
7. Copy the configuration values to your `.env.local` file

### Firebase Admin (Optional - for Persistent Vault Storage)

For persistent vault storage that survives server restarts, add one of the following to `backend/.env`:

**Option 1: Service Account JSON (Recommended)**

```env
FIREBASE_SERVICE_ACCOUNT={"type":"service_account","project_id":"...","private_key_id":"...","private_key":"...","client_email":"...","client_id":"...","auth_uri":"...","token_uri":"...","auth_provider_x509_cert_url":"...","client_x509_cert_url":"..."}
```

**Option 2: Project ID (uses application default credentials)**

```env
FIREBASE_PROJECT_ID=your-project-id
```

**Option 3: Path to service account JSON file**

```env
GOOGLE_APPLICATION_CREDENTIALS=./path/to/service-account-key.json
```

**Note:** Without Firebase Admin, vault uses in-memory storage (data resets on server restart). To get a Firebase service account:

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Go to Project Settings > Service Accounts
4. Click "Generate New Private Key"
5. Copy the JSON content to `FIREBASE_SERVICE_ACCOUNT` in your `.env` file

## 🐛 Troubleshooting

### Backend Offline

1. Ensure backend is running: `cd backend && pnpm run dev`
2. Check health: Visit `http://localhost:5000/api/health`
3. Verify port 5000 is not in use

### Port Already in Use

**Windows:**

```powershell
netstat -ano | findstr :5000
taskkill /PID <PID> /F
```

**Linux/Mac:**

```bash
lsof -ti:5000 | xargs kill
```

### Frontend Won't Start

```bash
cd frontend
rm -rf node_modules
pnpm install
pnpm run dev
```

## 📁 Project Structure

```
MindBrief-AI_Summarizer/
├── backend/                    # Express.js API server
│   ├── src/
│   │   ├── routes/            # API route handlers
│   │   │   ├── auth.js        # Authentication routes
│   │   │   ├── search.js      # Search and scraping routes
│   │   │   ├── summarize.js   # AI summarization routes
│   │   │   └── vault.js       # Vault storage routes
│   │   └── server.js          # Main server file
│   ├── package.json           # Backend dependencies
│   ├── requirements.txt       # Backend requirements list
│   └── .env                   # Environment variables (create this)
├── frontend/                   # Next.js frontend
│   ├── src/
│   │   ├── app/               # Next.js app router pages
│   │   │   ├── dashboard/     # Main dashboard page
│   │   │   ├── login/         # Login/signup page
│   │   │   └── page.jsx       # Home page
│   │   ├── components/        # React components
│   │   │   ├── vault.jsx     # Vault component
│   │   │   ├── auth-guard.jsx # Auth protection
│   │   │   └── ui/           # UI components
│   │   └── lib/              # Utilities
│   │       ├── api.js        # API client
│   │       ├── firebase.js   # Firebase config
│   │       └── auth-context.jsx # Auth context
│   ├── package.json           # Frontend dependencies
│   ├── requirements.txt       # Frontend requirements list
│   └── .env.local            # Frontend env vars (create this)
├── README.md                   # This file
└── workflow.txt               # Detailed project workflow
```

## 🔧 Available Scripts

### Backend

- `pnpm start` - Start production server
- `pnpm run dev` - Start with auto-reload

### Frontend

- `pnpm run dev` - Start development server
- `pnpm run build` - Build for production
- `pnpm start` - Start production server

## 🌟 API Endpoints

- `GET /api/health` - Health check
- `POST /api/search` - Search for websites
- `POST /api/search/scrape` - Scrape content from URL
- `POST /api/summarize` - Generate summary from URLs
- `GET /api/vault/:userId` - Get user's vault items
- `POST /api/vault/:userId` - Save item to vault
- `DELETE /api/vault/:userId/:itemId` - Delete vault item

## 📦 Installation from Requirements

### Backend

```bash
cd backend
pnpm install
# Or use requirements.txt as reference
```

### Frontend

```bash
cd frontend
pnpm install
# Or use requirements.txt as reference
```

## 🔍 How It Works

1. **Search**: Uses DuckDuckGo to find relevant websites for your topic
2. **Scraping**: Intelligently extracts main content from selected URLs
3. **AI Processing**: Sends content to Google Gemini API for summarization
4. **Storage**: Saves summaries to Firebase Firestore or in-memory storage
5. **Export**: Allows downloading summaries as TXT or Markdown files

## 📋 Complete Project Workflow

### Architecture Overview

- **Frontend**: Next.js 15 with React 19, JavaScript (JSX), Tailwind CSS  
- **Backend**: Express.js with Node.js  
- **AI**: Google Gemini API for summarization  
- **Database**: Firebase Firestore (optional, with in-memory fallback)  
- **Authentication**: Firebase Authentication  


### 1. Authentication Flow

```
User visits app → Login/Sign Up page
  ↓
User authenticates via Firebase Auth
  ↓
Auth state checked by AuthGuard component
  ↓
If authenticated → Redirect to Dashboard
If not authenticated → Stay on Login page
```

### 2. Search Flow

```
User enters topic in search box
  ↓
Frontend: api.search.searchTopic(topic)
  ↓
Backend: POST /api/search
  ↓
Backend scrapes DuckDuckGo search results
  ↓
Backend extracts URLs and titles
  ↓
Backend filters out search engine URLs
  ↓
Backend returns list of links
  ↓
Frontend displays links in sidebar
```

### 3. Link Selection Flow

```
User clicks on links in sidebar
  ↓
Frontend: toggleLinkSelection(url)
  ↓
Selected links stored in state (selectedLinks array)
  ↓
Selected links highlighted with checkmark
  ↓
User can select/deselect multiple links
```

### 4. Summarization Flow

```
User clicks "Generate Summary" button
  ↓
Frontend: api.summarize.summarizeUrls(topic, selectedLinks)
  ↓
Backend: POST /api/summarize
  ↓
Backend validates URLs (skips search result pages)
  ↓
Backend scrapes content from each URL in parallel:
  - Sends HTTP GET request with proper headers
  - Parses HTML with Cheerio
  - Extracts main content (article, main, content containers)
  - Removes unwanted elements (nav, footer, ads, etc.)
  - Cleans and formats content
  ↓
Backend filters valid content (length > 30 chars)
  ↓
If Gemini API key configured:
  - Combines all content
  - Sends to Gemini API (gemini-2.5-flash model)
  - Gets AI-generated summary
Else:
  - Creates formatted summary from scraped content
  ↓
Backend returns summary with sources
  ↓
Frontend displays summary in main content area
```

### 5. Vault Storage Flow

```
User clicks "Save to Vault" button
  ↓
Frontend: api.vault.saveItem(userId, {topic, summary, sources})
  ↓
Backend: POST /api/vault/:userId
  ↓
Backend checks Firebase Admin initialization:
  If Firebase Admin configured:
    - Saves to Firestore collection "vaults"
    - Document ID = userId
    - Stores items array with new item
  Else:
    - Saves to in-memory Map (inMemoryVault)
  ↓
Backend returns success response
  ↓
Frontend shows success message
  ↓
Vault automatically opens
```

### 6. Vault Retrieval Flow

```
User clicks "Vault" button
  ↓
Frontend: api.vault.getItems(userId)
  ↓
Backend: GET /api/vault/:userId
  ↓
Backend checks Firebase Admin:
  If Firebase Admin configured:
    - Queries Firestore for user's vault document
    - Returns items array
  Else:
    - Returns items from in-memory Map
  ↓
Frontend displays list of saved summaries
  ↓
User can click on any summary to view details
  ↓
User can delete summaries
```

### 7. Download Flow

```
User clicks download button (TXT or Markdown)
  ↓
Frontend: downloadText() or downloadMarkdown()
  ↓
Creates blob with summary content
  ↓
Creates download link
  ↓
Triggers browser download
  ↓
File saved to user's downloads folder
```

### Request Flow Architecture

```
Frontend (React Component)
  ↓
Frontend API Client (lib/api.ts)
  ↓
HTTP Request to Backend
  ↓
Backend Express Router
  ↓
Route Handler (routes/*.js)
  ↓
Business Logic
  ↓
External Services (Gemini API, Firebase, Web Scraping)
  ↓
Response back through chain
```

### Error Handling

**Frontend Error Handling:**
- API request fails → Caught in `try-catch` block  
- Error logged in console  
- User-friendly alert message shown  
- Backend status updated to **offline** if unreachable  

**Backend Error Handling:**
- Route handler catches errors using `try-catch`  
- Error details logged on server  
- Appropriate HTTP status code returned  
- Error message sent back in JSON response  

**Scraping Error Handling:**
- URL scraping fails → Error caught in scrape promise  
- Returns error object with URL and error message  
- Failed URLs are filtered out  
- If all URLs fail → Return error response  
- If some URLs succeed → Continue with valid content  

---

### Security Flow

**Authentication:**
- User login/signup → Firebase Authentication validates credentials  
- User session stored in browser  
- Protected routes check authentication state  
- API requests include user ID from auth context  

**Rate Limiting:**
- Request reaches backend  
- Express rate-limiter middleware checks request count  
- If within limit → Request processed  
- If exceeded → Returns **429 Too Many Requests**  

**CORS:**
- Frontend sends request  
- Backend CORS middleware checks request origin  
- If origin is allowed → CORS headers added  
- Request proceeds to route handler  

---

### Performance Optimizations

1. **Parallel Scraping** – Multiple URLs scraped simultaneously  
2. **Content Filtering** – Unnecessary elements removed early  
3. **Caching** – In-memory vault for fast access  
4. **Rate Limiting** – Prevents excessive requests  
5. **Error Recovery** – Graceful fallback when some URLs fail  

---

## 🛡️ Security Features

- **Rate Limiting** – Prevents API abuse  
- **CORS Protection** – Allows requests only from configured frontend  
- **Helmet.js** – Adds secure HTTP headers  
- **Firebase Authentication** – Secure user login system  
- **Input Validation** – Validates all user inputs before processing  

## 📊 Technology Stack

- **Frontend**: Next.js 15, React 19, JavaScript (JSX), Tailwind CSS, Shadcn UI, Framer Motion  
- **Backend**: Express.js, Node.js  
- **AI**: Google Gemini API (gemini-2.5-flash)  
- **Database**: Firebase Firestore (optional) / In-Memory Storage  
- **Authentication**: Firebase Authentication  
- **Scraping**: Axios, Cheerio, jsdom, @mozilla/readability  
- **Security & Validation**: Helmet, CORS, Joi  
- **State & Networking**: TanStack Query, React Hook Form  

## 🚀 Deployment

### Backend Deployment

1. Set environment variables on your hosting platform
2. Run `pnpm install` in the backend directory
3. Start server with `pnpm start`

### Frontend Deployment

1. Set environment variables (NEXT_PUBLIC_*)
2. Run `pnpm run build`
3. Deploy the `.next` folder or use `pnpm start`

### Recommended Platforms

- **Vercel**: Great for Next.js frontend
- **Railway/Render**: Good for backend Express.js
- **Firebase Hosting**: Can host both with proper configuration

## 📝 Notes

- Backend must be running before using the frontend
- Vault storage is in-memory by default (configure Firebase for persistence)
- AI summarization requires Gemini API key for best results
- All web scraping is done server-side to avoid CORS issues
- See `workflow.txt` for detailed project workflow documentation

## 🤝 Contributing

Contributions are welcome!  
Please feel free to submit a Pull Request.

---

## 📄 License

MIT License.

---

## 👨‍💻 Author

**Kaif Khan**  
GitHub: https://github.com/sak11mis04-hub

---

✨ **Happy Summarizing!**

For detailed workflow information, see [workflow.txt](./workflow.txt)
