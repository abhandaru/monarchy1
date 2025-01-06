# Authentication and authorization

Some notes on how we handle authentication and authorization. In general, we focus little on authorization since most objects in the game are public. Some simple filtering is used to check for mutations to user data or shared objects like game state.

## Discord

The primary authentication method for the game is through Discord. The plumbing is there to implement our own authorization server, but we feel that engagement will be much higher via OAuth2.

We have two applications registered with Discord:

- [`monarchy`](https://discord.com/developers/applications/1323331352283709491/oauth2) - the main application, used for production
- [`monarchy-staging`](https://discord.com/developers/applications/1325545581304614912/oauth2) - a separate application for test environments

## Future work

We can add other OAuth2 providers. A field to track the OpenID of the `user` will be added. Then adding new mappings from public keys to an issuing service should be straightforward.