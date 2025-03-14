<div align="center"><img width="200" alt="Companero" src="https://companero.io/assets/images/logo/logo.webp"/></div>
<br/>
<p align="center">
   <i>Compañero is an open-source ridesharing and hitchhiking service. Find a travel companion without any expenses!</i>
   <br/>
   POC: <a href="https://t.me/CompaneroBot">https://t.me/CompaneroBot</a>
   <br/><br/>
     <b><a href="https://companero.io">Website</a></b>  | <b><a href="https://t.me/CompaneroUpdates">Public Channel</a></b> | <b><a href="https://t.me/CompaneroChat">Chat</a></b>
     <br/><br/>
   <a href="https://jdk.java.net/archive/"><img src="https://img.shields.io/badge/Java_Version-21-ffd7d7?logo=hackthebox&logoColor=fff" alt="JavaVersion"/></a>
   <a target="_blank" href="https://github.com/kopytovskiy/companero-bot"><img src="https://img.shields.io/github/last-commit/kopytovskiy/companero-bot?logo=github&color=ffd7d7&logoColor=fff" alt="Last commit"/></a>
   <img src="https://img.shields.io/badge/PRs-Welcome-ffd7d7?&logoColor=fff" alt="PRs"/>   
   <a href="https://github.com/kopytovskiy/companero-bot/blob/master/LICENSE"><img src="https://img.shields.io/badge/License-AGPLv3-ffd7d7?logo=opensourceinitiative&logoColor=fff" alt="License AGPLv3"/></a>
   <br/><br/>
</p>


## Table of Contents
- [Features](#features)
- [How It Works](#how-it-works)
- [Getting started](#getting-started)
- [Our Philosophy](#our-philosophy)
- [Contributing](#contributing)
- [Sponsorship](#sponsorship)
- [License](#license)

## Features
* **Privacy**. No personal data needed to use our app.
* **Multi-language**. For now, we support 6 languages: English, Spanish, Ukrainian, Portuguese, French, German.
* **Free**. Compañero is completely free and open-source.
* **Cross-plarform**. Use it on any device where Telegram is installed.
* **Simplicity**. Request a ride just in several clicks and all drivers near-by will recieve it.
* **Price calculation**. You can always rely on our price calculations or set your own price, even request a free ride!
* **Rating system**. Rate drivers and passengers after each ride, fostering trust and safety within our community.

## How It Works
1. You set up a profile and choose a role (driver or passenger).
2. As a passenger, you can request a ride by sending the location of the pickup point and the destination point.
3. Also, as a passenger, you can provide extra information, set a price (we also calculate a recommended one for you 🥰), and choose a pickup time.
4. If you request a ride for now, all drivers in an area of **25 kilometers** around you will receive your request.
5. If you request a ride for later, all drivers in the area of **125 kilometers** around you will receive your request.
6. As a driver, you can see the pickup point, the approximate destination point, the price, and some extra information (**not personal data**).
7. You can accept this ride request and see the user's contacts that he provided. At the pickup point, you will see the full destination address.

## Getting started
1. Generate Telegram Token, using [@BotFather](https://t.me/BotFather).
2. Run your [MongoDB](https://github.com/mongodb/mongo) database.
3. Create "CompaneroBotDB" database in your MondoDB. (optional step)
4. Create collection "driversInfo" in your "CompaneroBotDB" database. (optional step)
5. Create "2dsphere" index for "location" field in "driversInfo" collection.
6. Run Compañero using next command: `mvn clean compile exec:java -Dexec.mainClass=com.companerobot.Main -Dbot_token=*token* -Dmongodb_client_url=*url* -Dcipher_algorithm=*algorithm* -Dcipher_key=*key*`
    * For `-Dcipher_algorithm` you can use "AES/ECB/PKCS5Padding" or any alternative to it.
7. Have fun 😉

## Our Philosophy
1. We believe in kindness and the power of human connection.
2. We believe that travel should be accessible to all, regardless of economic status.
3. We believe in environmentally conscious travelling approach and one of our aims is to reduce carbon footprints.
4. We believe that simplicity is a key.

## Contributing
We welcome contributions to our project! Here are some ways you can help:
* 🐛 Report bugs or suggest features
* 💻 Submit pull requests
* 🙋‍♂️ Vote for new features in our [public channel](https://t.me/CompaneroUpdates)
* 🔍 Perform code reviews
* 📖 Improve documentation
* 🧪 Add or improve tests
* 🌐 Help with translations
* 🤝 Provide support in discussions
* 🚀 Share the project
* ⭐ Star us on GitHub — it motivates us a lot!

A huge thank you to everyone who is helping to improve Compañero. Thanks to you, the project can evolve!

### Our Contributors

<a href="https://github.com/kopytovskiy"><img src="https://avatars.githubusercontent.com/u/17334798?v=4" width="50" height="50" alt=""/></a>

## Sponsorship

I don't ask for donations for this project, but I kindly request your support for Ukraine during these tough times 🇺🇦

The country and its people are facing a humanitarian disaster and need our help. If you find this project useful, please consider making a donation to reputable organizations providing aid to Ukraine. Every contribution, no matter how small, can make a significant difference. 

You can choose any fund you like or make a donation at [savelife.in.ua](https://savelife.in.ua/en/donate-en/). 

Thank you for your compassion and generosity 🙏

## License

This project is licensed under the GNU Affero General Public License v3.0 - see the [LICENSE](LICENSE) file for details.