// server.js
const express = require('express');
const path = require('path');
const cors = require('cors'); // Import the cors middleware
const app = express();
const port = 3000; // Or any port you prefer

// Enable CORS for all origins (you might want to restrict this in production)
app.use(cors());

// Serve static files from the React app's build directory
// The 'build' directory is relative to where your server.js is
app.use(express.static(path.join(__dirname, 'build')));

// Handle React routing, return all requests to the React app
app.get('*', function(req, res) {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

app.listen(port, () => {
  console.log(`Node.js server listening on port ${port}`);
});