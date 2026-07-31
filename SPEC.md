# MCU Tier List — Spec

## Overview

A simple monolithic web app to keep track of MCU movies, score them, visualize scores on a line chart, and organize them into a tier list. Built for learning purposes — not intended for production.

## Tech Stack

| Layer      | Choice                      |
|------------|-----------------------------|
| Language   | Java 25                     |
| Framework  | Spring Boot                 |
| Templates  | Thymeleaf                   |
| Styles     | Bootstrap 5                 |
| Charts     | Chart.js                    |
| Database   | SQLite (file-based)         |
| ORM        | Spring Data JPA + Hibernate |

## Authentication

- No registration flow — users are seeded manually in the DB
- Login: user enters their username (no password)
- Session stored in a cookie with a 4-hour expiry
- Each user sees only their own scores and ranking

## Features

### 1. Movies Tab (Main screen)
- Displays all MCU movies in a list/table: poster, title, alternative title, release date, phase, score, score label, tier
- User can set or update a movie's score (0.5–5.0 in 0.5 increments)
- User can drag to reorder movies **within the same tier only**
- Will show a table with the average scores per movie phase column as well the overall scores average
- No add or delete — the catalog is pre-seeded

### 2. Tier List Tab (Main screen)
- Read-only TierMaker-style view
- Each tier is a row with its label in the first column (Excellent → Bad, top to bottom)
- Movie posters fill the row left to right, ordered by the user's ranking within that tier
- Unrated movies are not shown
- No interaction — view only

### 3. Line Chart (Main screen, Movies tab or shared header)
- X-axis: rated movies ordered by tier then ranking
- Y-axis: score (0.5–5.0)
- Only rated movies appear
- Rendered with Chart.js

### 4. Scoring
- Scores range from 0.5 to 5.0 in 0.5 increments (10 possible values)
- Score labels are pre-seeded and shared across all users (not editable)
- Unrated movies appear in the movie list but not in the chart or tier list

### 5. Ranking
- Ranking is scoped per tier — a movie can only be reordered among movies in the same tier
- A 4.5-scored movie cannot be ranked above a 5.0-scored movie
- Display order is: tier (Excellent → Bad), then ranking within tier

## Tier Definitions

| Score Range  | Tier      |
|--------------|-----------|
| 4.5 – 5.0    | Excellent |
| 4.0 – 4.49   | Very Good |
| 3.0 – 3.99   | Good      |
| 2.0 – 2.99   | Weak      |
| 0.5 – 1.99   | Bad       |
| Unrated      | —         |

## Data Model

### `User`
| Field    | Type   | Notes                     |
|----------|--------|---------------------------|
| username | String | Unique PK, used for login |
| name     | String | Display name              |

### `Movie`
| Field             | Type      | Notes                              |
|-------------------|-----------|------------------------------------|
| id                | Long      | Auto-generated PK                  |
| original_title    | String    | Required                           |
| alternative_title | String    | International/alternate name       |
| release_date      | LocalDate | Required                           |
| phase             | Integer   | MCU phase number                   |
| poster_url        | String    | URL to poster image                |

### `ScoreLabel`
| Field        | Type       | Notes                              |
|--------------|------------|------------------------------------|
| score        | BigDecimal | PK; 0.5–5.0 in 0.5 steps          |
| description  | String     | Internal description               |
| display_name | String     | Label shown in the UI              |

### `UserMovieScore`
| Field    | Type       | Notes                                        |
|----------|------------|----------------------------------------------|
| id       | Long       | Auto-generated PK                            |
| username | String     | FK → User                                    |
| movie_id | Long       | FK → Movie                                   |
| score    | BigDecimal | Nullable; 0.5–5.0 in 0.5 steps              |
| ranking  | Integer    | Nullable; position within tier, user-defined |

## Screens & Routes

| Method | Route                           | Description                                      |
|--------|---------------------------------|--------------------------------------------------|
| GET    | `/login`                        | Login page (username form)                       |
| POST   | `/login`                        | Authenticate and set session cookie              |
| POST   | `/logout`                       | Clear session cookie                             |
| GET    | `/`                             | Main page — Movies tab (list + chart)            |
| GET    | `/?tab=tierlist`                | Main page — Tier List tab                        |
| POST   | `/movies/{id}/score`            | Set or update a movie's score                    |
| POST   | `/movies/{id}/rank`             | Update ranking within tier (drag-and-drop)       |

## Seeded Data
- All MCU movies are pre-loaded in the `Movie` table
- All 10 score labels (0.5–5.0) are pre-loaded in the `ScoreLabel` table
- Users are manually inserted into the `User` table

## Out of Scope
- Passwords or any real security
- User registration
- External movie search APIs (e.g. TMDB)
- Adding or deleting movies
- Editing score labels
- Mobile-specific layout
