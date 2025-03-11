// Create the userData database (if it doesn't exist)
db = db.getSiblingDB('userData');
db.grantRolesToUser(
    "root",
    [
        { role: "readWrite", db: "userData" },
        { role: "read", db: "admin" }
    ]
)
