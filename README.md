# IPLogin
### Basic PaperMC plugin for IP login
When you first log in, the plugin stores your username and IP, and the next time someone joins with your username, if the IP is correct, nothing happens, but if the IP is incorrect, it gets banned.

Alts are also banned, if allowalts is false and a new player has the IP of an already existing player, the IP gets banned.

The command /setip <newip> changes your stored IP to a new one. After you log out, this IP will be used to verify your logins.

The command /allowalts <value> allows/disallows alts. Alts that login after this command is executed will be banned.

You can also change allow-alts from the config file.
