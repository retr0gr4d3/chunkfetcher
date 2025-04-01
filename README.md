# ChunkFetcher

A Paper plugin for Minecraft 1.21.4 that adds a special hopper which collects all items dropped within a chunk.

## Features

- **Chunk Fetcher Hopper**: A special hopper that collects all items dropped within the chunk it's placed in
- **Maintains Normal Hopper Functionality**: Still pushes items into containers it's connected to
- **Crafting Recipe**: Craft by surrounding an Ender Pearl with 8 Hoppers
- **Admin Command**: Give Chunk Fetcher hoppers to players using admin commands

## Installation

1. Download the latest release from the releases section
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin if needed (no configuration required by default)

## Usage

### Crafting

Craft a Chunk Fetcher by placing 8 hoppers around an ender pearl in a crafting table.

```
H H H
H E H
H H H
```

Where:
- H = Hopper
- E = Ender Pearl

### Admin Commands

- `/chunkfetcher add <player> <amount>` - Gives the specified player a certain amount of Chunk Fetcher hoppers
  - Permission: `chunkfetcher.admin`

## How It Works

1. Place a Chunk Fetcher hopper anywhere in a chunk
2. The hopper will automatically collect all items that drop within that chunk
3. Connect the hopper to a container (such as a chest) to have items automatically transferred

## Permissions

- `chunkfetcher.admin` - Allows the use of admin commands (default: op)

## Building from Source

1. Clone the repository
2. Build using Maven: `mvn clean package`
3. Find the compiled JAR in the `target` directory

## License

This plugin is released under the MIT License. See the LICENSE file for details. 