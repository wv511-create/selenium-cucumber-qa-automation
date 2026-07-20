# Product Requirements Document (PRD): Namma-Yantra Share

## 1. Project Overview
**Namma-Yantra Share** is a peer-to-peer tractor and heavy machinery marketplace designed to bridge the gap between machinery owners and farmers. The platform allows farmers to rent equipment at affordable rates while helping owners monetize their idle machinery.

## 2. Target Audience
- **Farmers (Renters)**: Small to medium-scale farmers looking for affordable machinery for seasonal work.
- **Machinery Owners (Providers)**: Individuals or businesses owning tractors, harvesters, etc., looking for additional income.

## 3. Key Features

### 3.1 Farmer Module
- **Browse & Filter**: View available machinery (Tractors, Harvesters, Tillers) with distance and rating.
- **Price Calculator**: Real-time rental estimation based on duration (hourly/daily).
- **Booking Flow**: Simple request system to book machinery with purpose specification.
- **Booking History**: Track status of current and past rental requests.

### 3.2 Owner Module
- **Dashboard**: High-level overview of active machines and pending requests.
- **Inventory Management**: Add, update, and manage machinery details and availability.
- **Request Management**: Accept or decline incoming booking requests from farmers.
- **Analytics**: Track earnings and machine performance.

## 4. Technical Stack
- **Frontend**: Kotlin, Jetpack Compose (Android), HTML5/CSS3/JavaScript (Web Prototype).
- **Backend/Database**: Firebase Authentication, Cloud Firestore (Real-time data sync).
- **Architecture**: MVVM (Model-View-ViewModel) for the Android application.

## 5. User Journey
1. **Onboarding**: User selects role (Farmer or Owner).
2. **Farmer Flow**: Browse -> Select Machine -> Calculate Price -> Send Booking Request.
3. **Owner Flow**: Dashboard -> View Request -> Accept/Decline -> Update Machine Status.

## 6. Future Scope
- **GPS Tracking**: Real-time tracking of machinery during rental.
- **Payment Gateway**: Integrated digital payments (UPI/Wallets).
- **Rating System**: Two-way feedback for both farmers and owners.
