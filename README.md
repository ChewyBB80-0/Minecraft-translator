# Minecraft Universal Translator Plugin

A Minecraft plugin that allows players who speak different languages to communicate seamlessly in-game. Each player sees messages translated into their preferred language automatically.

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
