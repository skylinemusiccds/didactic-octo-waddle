
<div align="center">
	<img src="https://raw.githubusercontent.com/skylinemusiccds/audioflare/dev/fastlane/metadata/android/en-US/images/17.png">
<h1>AudioFlare</h1>A FOSS YouTube Music client for Android with many features from<br>Spotify, Musixmatch, SponsorBlock, ReturnYouTubeDislike<br>
<br>
<a href="https://github.com/skylinemusiccds/audioflare/releases"><img src="https://img.shields.io/github/v/release/universe-dev/audioflare"></a>
<a href="https://github.com/skylinemusiccds/audioflare/releases"><img src="https://img.shields.io/github/downloads/universe-dev/audioflare/total"></a>
<br>
<br>
<a href="https://apt.izzysoft.de/packages/com.universe.audioflare/"><img src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png" height="80"></a>
<a href="https://f-droid.org/en/packages/com.universe.audioflare/"><img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" height="80"></a>
<a href="https://github.com/skylinemusiccds/audioflare/releases"><img src="https://raw.githubusercontent.com/NeoApplications/Neo-Backup/034b226cea5c1b30eb4f6a6f313e4dadcbb0ece4/badge_github.png" height="80"></a>
<h3>Nightly Build<h3>
<a href="https://nightly.link/skylinemusiccds/audioflare/workflows/android/dev/app.zip"><img src="https://github.com/skylinemusiccds/audioflare/actions/workflows/android.yml/badge.svg"></a><br/>
<a href="https://nightly.link/skylinemusiccds/audioflare/workflows/android/dev/app.zip"><img src="https://raw.githubusercontent.com/NeoApplications/Neo-Backup/034b226cea5c1b30eb4f6a6f313e4dadcbb0ece4/badge_github.png" height="80"></a>
</div>
	
## Features ✨️

- Play music from YouTube Music or YouTube free without ads in the background
- Browsing Home, Charts, Podcast, Moods & Genre with YouTube Music data at high speed
- Search everything on YouTube
- Analyze your playing data, create custom playlists, and sync with YouTube Music...
- Spotify Canvas supported
- Play video option with subtitle
- AI suggest songs
- Caching and can save data for offline playback
- Synced lyrics from Musixmatch, Spotify (require login) and YouTube Transcript and translated lyrics (Community translation from Musixmatch)
- Personalize data (*) and multi-YouTube-account support
- Support SponsorBlock, Return YouTube Dislike
- Sleep Timer
- Android Auto with online content
- And many more!

> (*) For users who chose "Send back to Google" feature

> **Warning**

>This app is in the beta stage, so it may have many bugs and make it crash. If you find any bugs,
> please create an issue or contact me via email or discord sever.

## Screenshots

<p align="center">  
  <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/2.jpg?raw=true" width="200" />  
  <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/3.jpg?raw=true" width="200" />  
   <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/4.jpg?raw=true" width="200" />  
   <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/5.jpg?raw=true" width="200" />  
</p>  
<p align="center">  
  <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/6.jpg?raw=true" width="200" />  
  <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/7.jpg?raw=true" width="200" />  
   <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/8.jpg?raw=true" width="200" />  
   <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/9.jpg?raw=true" width="200" />  
</p>  
</p>  
<p align="center">  
  <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/10.jpg?raw=true" width="200" />  
  <img src="https://github.com/skylinemusiccds/audioflare/blob/dev/fastlane/metadata/android/en-US/images/phoneScreenshots/11.jpg?raw=true" width="200" /> 
</p>  

#### More [screenshot](https://photos.app.goo.gl/AbieoXG5ctDrpwzp7) here.

## Data

- This app uses hidden API from YouTube Music with some tricks to get data from YouTube Music.
- Use Spotify Web API and some tricks to get Spotify Canvas and Lyrics 
- Thanks to [InnerTune](https://github.com/z-huang/InnerTune/) for the idea to get data from YouTube Music. This repo is my inspiration to create this app
- My app is using [SponsorBlock](https://sponsor.ajay.app/) to skip sponsor in YouTube videos.
- ReturnYouTubeDislike for getting information on votes
- Lyrics data from Musixmatch. More information [Musixmatch](https://developer.musixmatch.com/)

## Privacy

audioflare doesn't have any tracker or third-party server for collecting user data. If YouTube
logged-in users enable "Send back to Google" feature, audioflare only uses YouTube Music Tracking API
to send listening history and listening record of video to Google for better recommendations and
supporting artist or YouTube Creator (For API reference,
see [this](https://github.com/skylinemusiccds/audioflare/blob/13f7ab6e5fa521b62a9fd31a1cefdc2787a1a8af/kotlinYtmusicScraper/src/main/java/com/universe/kotlinytmusicscraper/Ytmusic.kt#L639C4-L666C1)).

## Translation

[![Crowdin](https://badges.crowdin.net/audioflare/localized.svg)](https://crowdin.com/project/audioflare)  
You can help me translate this app into your language by using Crowdin [audioflare on Crowdin](https://crowdin.com/project/audioflare)

## FAQ

#### 1. Wrong Lyrics?

YouTube Music is not an official partner of Musixmatch so you can't get lyrics directly if using YouTube"
videoId" parameter. So I need to use some "String Matcher" and "Duration" for search lyrics. So
sometimes, some songs or videos get the wrong lyric's

#### 2. Why the name or brand is "audioflare"?

Simply, because I love this name. It's a combination of Simple and Music. But audioflare is not a simple app, it's all you need for a powerful music streaming app.

## Developer/Team

### [UniVerse Ai Systems](https://github.com/universe-dev/audioflare): Founder/Developer/Designer  

## Support & Donations
<div align="left">
<a href="https://audioflare.tech/"><img alt="Visit the website" height="50" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/documentation/website_vector.svg"></a>
&nbsp;
<a href="https://discord.gg/Rq5tWVM9Hg"><img alt="Discord Server" height="50" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-plural_vector.svg"></a>
&nbsp;
<br>
<a href="https://www.buymeacoffee.com/universe"><img alt="Buy me a Coffee" height="50" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/donate/buymeacoffee-singular_vector.svg"></a>
&nbsp;
<a href="https://liberapay.com/universe/"><img alt="liberapay" height="50"
src="https://raw.githubusercontent.com/liberapay/liberapay.com/master/www/assets/liberapay/logo-v2_black-on-yellow.svg"></a>
<div/>
