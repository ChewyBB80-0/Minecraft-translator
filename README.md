# Minecraft Universal Translator Plugin

Copyright © 2026 ChewyBB80-0. All Rights Reserved.

## License

This software is proprietary and copyrighted. Unauthorized use, modification, distribution, or redistribution is strictly prohibited.

### You may NOT:
- Use this software without explicit written permission from ChewyBB80-0
- Modify, adapt, or create derivative works
- Distribute, sell, or share this software
- Remove or alter copyright notices
- Use this software in commercial projects without permission

### You MAY:
- Use this software on a personal or private server with permission
- Report bugs and submit feature requests
- Contribute via pull requests (requires approval)

### Permission Requests:
Contact ChewyBB80-0 on GitHub for licensing inquiries:
https://github.com/ChewyBB80-0

## Features

- **Real-Time Chat Translation** - Messages are automatically translated to each player's preferred language
- **Language Selection** - Players can choose their preferred language via `/language` command
- **Smart Cache System** - Translations are cached to reduce API costs
- **Staff Controls** - Moderators can bypass translation to see original messages
- **Translation Logs** - Optional logging for moderation investigations
- **Discord Integration** - Translate messages between Minecraft and Discord (Phase 2)

## Supported Languages (v1)

- English (en)
- Spanish (es)
- French (fr)
- German (de)
- Portuguese (pt)
- Russian (ru)
- Japanese (ja)
- Korean (ko)
- Chinese (zh)

## Commands

- `/language` or `/lang` - Open language selection GUI
- `/translate on/off` - Toggle translation for a player
- `/translate bypass` - Staff bypass to see original messages

## Configuration

```yaml
default-language: en
show-original-hover: true
cache-enabled: true
cache-expiry-hours: 48
supported-languages:
  - en
  - es
  - fr
  - de
  - pt
  - ru
  - ja
  - ko
  - zh
```

## Building

Requires Maven and Java 17+.

```bash
mvn clean package
```

The JAR will be generated in `target/` directory.

## Installation

1. Place the JAR in your server's `plugins/` folder
2. Configure the plugin settings in `plugins/Minecraft-translator/config.yml`
3. Restart the server

## License

MIT License
