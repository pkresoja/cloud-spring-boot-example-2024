# Data Cache

Powerful and fast caching service for [SuperLink](https://github.com/Pequla/SuperLink)

> All data is cached for the maximum of 2h or if the specific amount of requests has been reached

## Status codes

- HTTP `404 NOT FOUND` - Returns empty body
- HTTP `500 INTERNAL SERVER ERROR` - Returns error object

Error object example:
```json
{
    "name": "RuntimeException",
    "message": "User not found",
    "path": "/api/data/uuid/06805a4280d0463dbf7151b1e1317cd4",
    "timestamp": 1656578263538
}
```
## Endpoints

Application supports a big list of endpoints with loads of customization to suite everyone's needs
> Any endpoint that supports pagination can be manipulated using query params `page`, `size` and `sort`
> 
> Example usage: `?page=3&size=12&sort=id.desc`

- GET `/api/data` - Paged list of all data

```json
{
  "content": [
    {
      "id": 1,
      "name": "nikeq",
      "uuid": "17933cb0b20f463e966f4d59518a2c7c",
      "discordId": "667897322742743040",
      "tag": "finley#0141",
      "avatar": "https://cdn.discordapp.com/avatars/667897322742743040/ed4481fe121922a6aacadf6aab5a4e16.png",
      "guildId": "264801645370671114",
      "createdAt": "2021-06-29T14:58:14"
    },
    {
      "id": 2,
      "name": "GDLudus",
      "uuid": "60d1d626909b419fbcd824a7269bfdb0",
      "discordId": "360217670639026187",
      "tag": "Ludus#5299",
      "avatar": "https://cdn.discordapp.com/avatars/360217670639026187/f7e9964277991dcb1aaf91340ea07eca.png",
      "guildId": "264801645370671114",
      "createdAt": "2021-06-29T15:24:22"
    },
    {
      "id": 3,
      "name": "P19E",
      "uuid": "a19513154af94ffb8b69562080b37e81",
      "discordId": "687297991358546054",
      "tag": "nikkekikkeli#3007",
      "avatar": "https://cdn.discordapp.com/avatars/687297991358546054/973e9ffb9bd79810916580e76c6017ee.png",
      "guildId": "264801645370671114",
      "createdAt": "2021-06-29T15:24:28"
    }
  ],
  "pageable": {
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 3,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 218,
  "totalElements": 653,
  "size": 3,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 3,
  "empty": false
}
```
- GET `/api/data/{id}` - Data for specific ID

> All IDs match the original API for simpler integration

```json
{
      "id": 1,
      "name": "nikeq",
      "uuid": "17933cb0b20f463e966f4d59518a2c7c",
      "discordId": "667897322742743040",
      "tag": "finley#0141",
      "avatar": "https://cdn.discordapp.com/avatars/667897322742743040/ed4481fe121922a6aacadf6aab5a4e16.png",
      "guildId": "264801645370671114",
      "createdAt": "2021-06-29T14:58:14"
}
```

- GET `/api/data/discord/{id}` - Data for specific Discord ID
- GET `/api/data/guild/{id}` - Paged list of data for specific Guild ID
- GET `/api/data/uuid/{uuid}` - Data for specific UUID
- GET `/api/data/name/{username}` - Data for username
