# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

MCU Tier List — a monolithic Spring Boot web app for rating and ranking MCU movies into a tier list. Built for learning purposes, not production (see SPEC.md for full functional spec). Java 25, Spring Boot 4, Thymeleaf, Bootstrap 5, Chart.js, SQLite.

## Commands

- Run locally: `./mvnw spring-boot:run`
- Build: `./mvnw clean package`
- Run tests: `./mvnw test`
- Run a single test: `./mvnw test -Dtest=ClassName#methodName`

There are currently no test classes in `src/test`. Devtools is on the classpath, so the app auto-restarts on recompiled changes; `spring.thymeleaf.cache=false` means templates reload without a restart.

## Architecture

**Request flow**: `SessionInterceptor` (registered in `WebConfig`) gates every route except `/login` and static assets, redirecting to `/login` if `session.getAttribute("username")` is unset. There's no Spring Security — auth is just a username lookup in `LoginController` (no password) that stashes `username`/`name` in the `HttpSession`. Every downstream controller/service call is scoped by pulling `username` back out of the session and passing it through as a plain string parameter — there's no `Principal`/`Authentication` object.

**Single controller, service-heavy**: `MovieController` has three routes (`/`, `/movies/{id}/score`, `/movies/reorder`) that all delegate to `MovieService`. Nearly all business logic — score lookups, tier resolution, ranking, chart data — lives in `MovieService`, keyed off `username` per call rather than a persisted "current user" concept.

**Tiers are derived, not stored**: `UserMovieScore.score` is the only per-user field that's authoritative; tier (`Excellent`/`Very Good`/`Good`/`Weak`/`Bad`) is computed on the fly from score via `MovieService.resolveTier()` and the `TIER_RANGES`/`TIER_ORDER` constants at the top of `MovieService`. When changing tier boundaries, `TIER_RANGES` and `resolveTier()` must be kept in sync — they currently duplicate the same thresholds independently.

**Ranking is scoped per tier**: `ranking` on `UserMovieScore` is only meaningful within a movie's current tier. `updateScore()` recomputes ranking (append-to-end via a count query) whenever a score change moves a movie into a new tier; `reorderWithinTier()` (called from the drag-and-drop `/movies/reorder` endpoint) just rewrites ranking sequentially for the given ID list — it does not itself validate that all IDs belong to the same tier, so callers/templates are relied on to enforce that.

**Data model** (`entity/`): `User` (username PK, no password), `Movie` (pre-seeded catalog, no create/delete from the app), `ScoreLabel` (PK is the score itself, 0.5–5.0 step 0.5, seeded/shared across users), `UserMovieScore` (the only mutable per-user state: score + ranking, FK to `User` + `Movie`). `MovieScoreDTO` is the read-model that joins a movie with the current user's score/label/tier/ranking for template rendering.

**Persistence**: SQLite file at `mcu-tierlist.db` (repo root), driven through `hibernate-community-dialects`' `SQLiteDialect`. `spring.jpa.hibernate.ddl-auto=update` + `spring.sql.init.mode=always` + `spring.jpa.defer-datasource-initialization=true` means `data.sql` runs after Hibernate creates/updates the schema on every startup — seed data in `data.sql` should be idempotent (e.g. `INSERT OR IGNORE`) or startup will fail/duplicate on repeat runs. Note `mcu-tierlist.db` is currently untracked but *not* gitignored — don't assume it's disposable.

**Templates**: `templates/index.html` (under `src/main/resources`) renders three tabs (`movies`/`rate`/`tierlist`, validated against `MovieController.VALID_TABS`) from one Thymeleaf template driven by the `tab` query param and model attributes assembled in `MovieController.index()`. `templates/login.html` is the only unauthenticated view.

## Code Conventions

See @docs/CODE_STYLE.md

## Notes

- The repo root also has its own untracked `index.html`, `movies.csv`, and `movies.json`, separate from the Thymeleaf template — these look like reference/scratch data (not gitignored, not part of the build). Confirm with the user before treating them as disposable.
