# VoteWise: Candidate Insights

A comprehensive Android app that helps users discover, match, and analyze political candidates at federal, state, and county levels. Built with Kotlin and Jetpack Compose for a modern, responsive UI.

## Features

### Core Functionality
- **Political Quiz System**: Take a comprehensive quiz on political views to match with candidates
- **Candidate Search & Browse**: Search candidates by name, district, party, or office level
- **Detailed Candidate Reports**: Comprehensive profiles with positions, voting records, donor information, and news
- **Real-time Data Integration**: Pulls from multiple sources including FEC, OpenSecrets, VoteSmart, Ballotpedia, and news APIs
- **AI-Powered Insights**: Consistency scoring and sentiment analysis for candidate positions

### User Experience
- **Modern UI**: Material You design with dynamic theming and dark mode support
- **Accessibility**: High contrast, voice-over support, and large tappable areas
- **Offline Support**: Cached data for offline access to candidate information
- **Social Integration**: Optional X (Twitter) OAuth for personalized features

### Privacy & Security
- **GDPR/CCPA Compliant**: Clear privacy controls and data handling
- **Encrypted Storage**: Secure storage of user data
- **Non-partisan**: Clear disclaimers about data sources and neutrality

### Monetization
- **Freemium Model**: Basic features free, premium subscription for advanced features
- **In-App Purchases**: Individual premium reports and feature bundles
- **Non-intrusive Ads**: Google AdMob integration with clear labeling

## Technical Architecture

### Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository pattern
- **Database**: Room with SQLite
- **Networking**: Retrofit with OkHttp
- **Dependency Injection**: Hilt
- **Image Loading**: Coil
- **Charts**: MPAndroidChart
- **Maps**: Google Maps API

### Data Sources
- **FEC API**: Federal candidate and donor data
- **OpenSecrets API**: Detailed donor and industry information
- **VoteSmart API**: Voting records and candidate positions
- **Ballotpedia API**: Candidate announcements and bios
- **NewsAPI**: Recent news and articles
- **X API**: Social media sentiment and announcements

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 26+ (Android 8.0+)
- Kotlin 1.8.0+
- Gradle 7.0+

### API Keys Required
You'll need to obtain API keys for the following services:

1. **Google Civic Information API**
   - Get key from [Google Cloud Console](https://console.cloud.google.com/)
   - Enable Civic Information API

2. **Google Maps API**
   - Get key from [Google Cloud Console](https://console.cloud.google.com/)
   - Enable Maps SDK for Android

3. **FEC API**
   - Get key from [FEC API](https://api.open.fec.gov/developers/)
   - Free tier available

4. **OpenSecrets API**
   - Get key from [OpenSecrets](https://www.opensecrets.org/api/)
   - Free tier available

5. **VoteSmart API**
   - Get key from [VoteSmart](http://votesmart.org/)
   - Free tier available

6. **NewsAPI**
   - Get key from [NewsAPI](https://newsapi.org/)
   - Free tier available

7. **X API (Twitter)**
   - Get key from [Twitter Developer Portal](https://developer.twitter.com/)
   - Requires approval

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/votewise.git
   cd votewise
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Configure API Keys**
   Create a `local.properties` file in the root directory:
   ```properties
   VOTEWISE_FEC_API_KEY=your_fec_api_key_here
   GOOGLE_CIVIC_API_KEY=your_google_civic_api_key_here
   MAPS_API_KEY=your_google_maps_api_key_here
   OPENSECRETS_API_KEY=your_opensecrets_api_key_here
   VOTESMART_API_KEY=your_votesmart_api_key_here
   NEWS_API_KEY=your_news_api_key_here
   X_API_KEY=your_x_api_key_here
   X_API_SECRET=your_x_api_secret_here
   ```

4. **Build and Run**
   - Sync project with Gradle files
   - Build the project (Build → Make Project)
   - Run on device or emulator (Run → Run 'app')

### Firebase Setup (Optional)
For push notifications and analytics:

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add Android app to the project
3. Download `google-services.json` and place in `app/` directory
4. Uncomment Firebase dependencies in `build.gradle.kts`

## Project Structure

```
app/
├── src/main/java/com/votewise/
│   ├── data/
│   │   ├── api/                 # API service interfaces
│   │   ├── auth/                # Authentication managers
│   │   ├── database/            # Room database and DAOs
│   │   ├── model/               # Data models
│   │   └── repository/          # Repository implementations
│   ├── ui/
│   │   ├── components/          # Reusable UI components
│   │   ├── navigation/          # Navigation setup
│   │   ├── screens/             # Screen composables
│   │   ├── theme/               # App theming
│   │   └── viewmodel/           # ViewModels
│   ├── navigation/              # Navigation definitions
│   └── MainActivity.kt
├── src/main/res/
│   ├── drawable/                # App icons and images
│   ├── layout/                  # XML layouts (if any)
│   ├── values/                  # Strings, colors, themes
│   └── xml/                     # Configuration files
└── build.gradle.kts
```

## Key Features Implementation

### Quiz System
- Dynamic question loading from database
- Real-time scoring and candidate matching
- Category-based analysis
- Progress tracking and session management

### Candidate Matching
- Algorithm-based matching using user responses
- Weighted scoring by category importance
- Real-time match percentage calculation
- Historical match tracking

### Data Integration
- Multi-source data aggregation
- Real-time API synchronization
- Offline caching with Room
- Data freshness indicators

### Security & Privacy
- Encrypted local storage
- GDPR/CCPA compliance features
- User consent management
- Data export functionality

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Disclaimer

VoteWise is a non-partisan political research tool. All data is sourced from public APIs and databases. The app does not endorse any political candidates or parties. Users should verify information independently before making voting decisions.

## Support

For support, email support@votewise.app or create an issue in this repository.

## Roadmap

- [ ] iOS version development
- [ ] Advanced AI insights
- [ ] Community features
- [ ] Debate integration
- [ ] Voter registration integration
- [ ] Multi-language support


