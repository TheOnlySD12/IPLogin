[<img alt="forthebadge" height="50" src="https://forthebadge.com/images/badges/gluten-free.svg" width="250"/>](https://forthebadge.com)
[<img alt="forthebadge" height="50" src="https://forthebadge.com/images/badges/built-with-swag.svg" width="250"/>](https://forthebadge.com)
[<img alt="forthebadge" height="50" src="https://forthebadge.com/images/badges/works-on-my-machine.svg" width="250"/>](https://forthebadge.com)
# IPLogin
**IPLogin** is a PaperMC plugin that allows server owners to restrict access to their servers based on IP address.
## Features
- Restrict server access based on IP address
  - Ban/Kick alts or impersonators
- Option to allow specific IP addresses to bypass the restriction
  - Players with a dynamic IP
  - Players that often login from other locations
- Easy setup and configuration
## Getting Started
To use **IPLogin**, simply download and install the version of **IPLogin** for your PaperMC server's version. Note that currently there is only one version; the one for `1.19-1.19.4`.

When a player first joins the server, their IP will be associated with their username. From now on, the plugin will check anyone with that player's username to see if they match the stored IP.

By default, **IPLogin** will kick all players who do not have a matching IP address in the `name-to-ip-map` file. You can add an `*` next to a player's name to allow them to access the server with any IP.
### Example:
`Player:*`
`Player:127.0.0.1`
## Commands
`/allowalts` prints a message with the value of `allow-alts`

`/allowalts <true/false>` to change the value of `allow-alts`, needs operator

`/setip <newip>` to change your IP, sender needs to be a player

`/setip <player> <newip>` to change someone's IP, needs operator

I probably won't make a command for every option in the config, but be sure to let me know if you would like that feature!
### Tip
If you have a dynamic IP, you should do this:

`/setip *`

Note that this means that the plugin won't protect your account from impersonators.
## Configuration
Once you've installed the plugin, you can configure it by editing the `config.yml` file, which is located in `plugins/IPLogin`. The file should look like this:
```
allow-alts: false
ban-alts: false
alts-message: Alts are not allowed.
ban-impersonators: false
impersonators-message: Impersonators are not allowed.
welcome-back-message: 'Welcome back, '
welcome-message: 'Welcome, '
```
Here you can change the messages for alts and impersonators, you can make the plugin kick or ban impersonators or kick or ban alts, you can even allow alts.
## Security
**IPLogin** provides a simple and effective way to restrict server access based on IP address. However, it is important to remember that IP addresses can be easily changed. For example, someone can use _a free VPN_ to change their IP **to a random one**. Therefore, **IPLogin** should not be relied upon as the sole method of securing your server, you should also have moderators.
## Support
If you have any questions or issues with **IPLogin**, you can use [the issue tracker](https://github.com/TheOnlySD12/iplogin/issues) on GitHub.
## Credits
**IPLogin** was developed by [TheOnlySD12](https://github.com/TheOnlySD12) and is licensed under the **MIT License**. Special thanks to the following people for their contributions to the project:

- Me (Code Review)
- Myself (Ideas)
- I (Testing)

I hope you enjoy using **IPLogin**! Please don't hesitate to use [the issue tracker](https://github.com/TheOnlySD12/iplogin/issues) if you have any feedback or suggestions.
