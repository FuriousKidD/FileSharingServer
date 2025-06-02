# File Sharing Server

This project implements a client-server network system for uploading and downloading images.
---

## Features

- **Server** listens on port 5432 and handles client requests:
  - `LIST` — returns available images with IDs
  - `DOWN <ID>` — downloads the requested image
  - `UP <ID> <Name> <Size> <Image>` — uploads a new image and updates the server list

- **Client** provides a user-friendly GUI to:
  - Upload images to the server
  - Retrieve a list of available images
  - Download and display an image by entering its ID

---

## Demo Screenshots

![Client Upload Screen](screenshots/client-upload.png)  
*Client GUI for uploading images*

![Server Console](screenshots/server-console.png)  
*Server running and handling requests*

![Client Image Display](screenshots/client-image-display.png)  
*Downloaded image displayed on client*

---

## Getting Started

### Prerequisites

- Java JDK 17 installed and `JAVA_HOME` environment variable set
- JavaFX SDK 17 downloaded

### Batch Files Provided

To simplify building, running, and cleaning the project, three batch files are included:

| Batch File   | Description                                   | Usage                          |
|--------------|-----------------------------------------------|--------------------------------|
| `clean.bat`  | Cleans all compiled class files               | Run before rebuilding the project |
| `server.bat` | Compiles and runs the server application      | Run to start the server          |
| `client.bat` | Compiles and runs the client application      | Run to start the client GUI      |

### Usage

1. **Clean previous builds (optional but recommended):**

    ```batch
    clean.bat
    ```

2. **Start the server:**

    ```batch
    server.bat
    ```

3. **Start the client:**

    ```batch
    client.bat
    ```

---

## Project Structure

- `src/` - Source code for client and server  
- `bin/` - Compiled `.class` files  
- `docs/` - Documentation files  
- `lib/` - External libraries (JavaFX)  
- `batch/` - Batch files to build and run projects  

---

## Notes

- Adjust the `JAVA_HOME` and `JAVAFX_HOME` paths in the batch files to match your local environment before running.
- The client-server communication protocol is text-based for commands and binary for image data.

---
