[<img alt="forthebadge" height="50" src="https://forthebadge.com/images/badges/gluten-free.svg" width="250"/>](https://forthebadge.com)
[<img alt="forthebadge" height="50" src="https://forthebadge.com/images/badges/built-with-swag.svg" width="250"/>](https://forthebadge.com)
[<img alt="forthebadge" height="50" src="https://forthebadge.com/images/badges/works-on-my-machine.svg" width="250"/>](https://forthebadge.com)

# IPLogin

**IPLogin** is plugin that allows server owners to restrict access to their servers based on IP address. This plugin will work with PaperMC and PaperMC forks, but won't work with Spigot or CraftBukkit. Why are you using them anyway?

## Important Notice

1. If you are updating to **IPLogin 2.7** you will need to manually transfer the data from `name-to-ip-map.txt` to `name-to-ip.yml`. To do this you must add a space between the `:` and the IP.

2. If you are updating to **IPLogin 2.8** from **2.7** you will need to delete the config.yml file before starting the server with the plugin.

3. If you are updating to **IPLogin 2.8** from any version older than **2.7** you will need to do both steps 2 and 1.

### Example:

Turn `Player:127.0.0.1` into `Player: 127.0.0.1`

## Features

- Restrict server access based on IP address or username
  - Ban/Kick alts or impersonators or users with a certain username
- Option to allow specific IP addresses to bypass the restriction
  - Players with a dynamic IP
  - Players that often login from other locations
- Easy setup and configuration
- Custom player log

## Getting Started

To use **IPLogin**, simply download and install the version of **IPLogin** for your PaperMC server's version. It is recommended to install the latest version of **IPLogin**.

When a player first joins the server, their IP will be associated with their username. From now on, the plugin will check anyone with that player's username to see if they match the stored IP.

By default, **IPLogin** will kick all players who do not have a matching IP address in the `name-to-ip.yml` file. You can add an `*` next to a player's name to allow them to access the server with any IP. This is useful when someone has a dynamic IP or often logs in from other locations.

#### Example:

`Player: *`  
`Player: 127.0.0.1`

## Commands

`/allowalts` prints a message with the value of `allow-alts`  
`/allowalts <true/false>` to change the value of `allow-alts`, needs operator  
`/setip <newip>` to change your IP, sender needs to be a player  
`/setip <player> <newip>` to change someone's IP, needs operator

I probably won't make a command for every option in the config, but be sure to let me know if you would like that feature!

## Configuration

Once you've installed the plugin, you can configure it by editing the `config.yml` file, which is located in `plugins/IPLogin`. The file should look like this:
```
logger:
  basic: true
  custom: true
name-filter:
  enabled: false
  ban: false
  message: Your name is not allowed on this server.
  mode: 1
  filter:
    - example
  exceptions:
    - example2
allow-alts: false
ban-alts: false
alts-message: Alts are not allowed.
ban-impersonators: false
impersonators-message: Impersonators are not allowed.
welcome-back-message: 'Welcome back, '
welcome-message: 'Welcome, '
```
Here you can change the messages for alts and impersonators, you can make the plugin kick or ban impersonators or kick or ban alts, you can even allow alts.

The username filter has 2 modes: the first one kicks/bans the player if their username contains the exact filter and the second one kicks/bans them if their username in lower case matches the filter. You can add specific usernames to the exceptions list.

#### Example
Mode: `1`  
Filter: `example1`, `example2`  
Exceptions: `myexample1`, `myexample2`  
Players that get kicked/banned : `Notexample1`, `Theexample2`, `example12`, `myexample1`, `myexample2`  
Players that don't get kicked/banned: `Example1`, `Example2`, `exAmple21`

Mode: `2`  
Filter: `example1`, `example2`  
Exceptions: `MyExample1`, `MyExample2`  
Players that get kicked/banned : `Notexample1`, `Theexample2`, `example12`, `Example1`, `Example2`, `exAmple21`, `MyExample1`, `MyExample2`  
Players that don't get kicked/banned: `3xample1`, `Ex4mple2`, `exAmpl321`

The basic logger announces bans/kicks in chat and the custom logger saves them to `log.yml` where it stores the username, time, IP, reason, type and if the player is an alt the owner of the IP.

#### Example

```
example:
  23/06/1940 12:00:00:
    IP: 127.0.0.1
    Reason: Impersonator
    Type: Kick
example2:
  23/06/1940 12:00:00:
    IP: 127.0.0.1
    Reason: Username filter
    Type: Kick
example3:
  23/06/1940 12:00:00:
    IP: 127.0.0.1
    Reason: Alt
    Type: Kick
    Owner(s):
    - example0
```

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
