DROP TYPE IF EXISTS public."e_user_type" CASCADE;

DROP TYPE IF EXISTS public."e_user_status" CASCADE;

CREATE TYPE public."e_user_status" AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'NONE',
    'BANNED');

DROP TYPE IF EXISTS public."e_gender" CASCADE;

CREATE TYPE public."e_gender" AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER');

DROP TABLE IF EXISTS public.role CASCADE;
CREATE TABLE public.role (
                                 id SERIAL PRIMARY KEY,
                                 role_name varchar(20) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS public.user CASCADE;
CREATE TABLE public.tbl_user (
                                 id bigserial NOT NULL,
                                 display_name varchar(255) NOT NULL,
                                 date_of_birth date NOT NULL,
                                 gender public."e_gender" NOT NULL,
                                 phone varchar(255) NULL,
                                 email varchar(255) NULL,
                                 "password" varchar(255) NOT NULL,
                                 status public."e_user_status" NOT NULL,
                                 role_id INT NOT NULL REFERENCES public.role(id),
                                 avatar_url varchar(255) NULL,
                                 created_at timestamp(6) DEFAULT NOW(),
                                 updated_at timestamp(6) DEFAULT NOW(),
                                 CONSTRAINT tbl_user_pkey PRIMARY KEY (id)
);



INSERT INTO public.role (id, role_name) VALUES
                                                (1, 'ADMIN'),
                                                (2, 'ARTIST'),
                                                (3, 'LISTENER');

-- ============================================
-- ENUM Types
-- ============================================
DROP TYPE IF EXISTS public.e_song_status CASCADE;
CREATE TYPE public.e_song_status AS ENUM ('PUBLISHED', 'DRAFT', 'REMOVED');

DROP TYPE IF EXISTS public.e_privacy CASCADE;
CREATE TYPE public.e_privacy AS ENUM ('PUBLIC', 'PRIVATE');

DROP TYPE IF EXISTS public.e_device CASCADE;
CREATE TYPE public.e_device AS ENUM ('MOBILE', 'DESKTOP', 'WEB');
-- ============================================
-- Artist Table
-- ============================================
DROP TABLE IF EXISTS public.artist CASCADE;
CREATE TABLE public.artist (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               bio TEXT,
                               avatar_url TEXT,
                               country_id BIGINT REFERENCES public.country(id) ON DELETE SET NULL,
                               debut_year INT,
                               created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                               updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- ============================================
-- Album Table
-- ============================================
DROP TABLE IF EXISTS public.album CASCADE;
CREATE TABLE public.album (
                              id BIGSERIAL PRIMARY KEY,
                              title VARCHAR(255) NOT NULL,
                              artist_id BIGINT NOT NULL REFERENCES public.artist(id) ON DELETE CASCADE,
                              cover_url TEXT,
                              release_date TIMESTAMP WITHOUT TIME ZONE,
                              total_tracks INT DEFAULT 0,
                              description TEXT,
                              created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                              updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- ============================================
-- Song Table
-- ============================================
DROP TABLE IF EXISTS public.song CASCADE;
CREATE TABLE public.song (
                             id BIGSERIAL PRIMARY KEY,
                             title VARCHAR(255) NOT NULL,
                             artist_id BIGINT NOT NULL REFERENCES public.tbl_user(id) ON DELETE CASCADE,
                             album_id BIGINT REFERENCES public.album(id) ON DELETE SET NULL,
                             duration INT NOT NULL, -- seconds
                             genre_id BIGINT REFERENCES public.genre(id) ON DELETE SET NULL,
                             release_date timestamp(6),
                             cover_url TEXT,
                             audio_url TEXT,
                             status public.e_song_status NOT NULL DEFAULT 'PUBLISHED',
                             play_count BIGINT DEFAULT 0,
                             like_count BIGINT DEFAULT 0,
                             created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                             updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- ============================================
-- Playlist Table
-- ============================================
DROP TABLE IF EXISTS public.playlist CASCADE;
CREATE TABLE public.playlist (
                                 id BIGSERIAL PRIMARY KEY,
                                 title VARCHAR(255) NOT NULL,
                                 owner_id BIGINT NOT NULL REFERENCES public.tbl_user(id) ON DELETE CASCADE,
                                 cover_url TEXT,
                                 description TEXT,
                                 privacy public.e_privacy NOT NULL DEFAULT 'PUBLIC',
                                 created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                                 updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- Playlist-Song (Many-to-Many)
DROP TABLE IF EXISTS public.playlist_song CASCADE;
CREATE TABLE public.playlist_song (
                                      playlist_id BIGINT NOT NULL REFERENCES public.playlist(id) ON DELETE CASCADE,
                                      song_id BIGINT NOT NULL REFERENCES public.song(id) ON DELETE CASCADE,
                                      track_number INT NOT NULL,
                                      added_at TIMESTAMP DEFAULT NOW(),
                                      PRIMARY KEY (playlist_id, song_id)
);

-- ============================================
-- Playback History
-- ============================================
DROP TABLE IF EXISTS public.playback_history CASCADE;
CREATE TABLE public.playback_history (
                                         id BIGSERIAL PRIMARY KEY,
                                         user_id BIGINT NOT NULL REFERENCES public.tbl_user(id) ON DELETE CASCADE,
                                         song_id BIGINT NOT NULL REFERENCES public.song(id) ON DELETE CASCADE,
                                         played_at TIMESTAMP DEFAULT NOW(),
                                         device VARCHAR(20) CHECK (device IN ('MOBILE', 'DESKTOP', 'WEB')) NOT NULL DEFAULT 'WEB'
);

-- ============================================
-- Like / Favorite
-- ============================================
DROP TABLE IF EXISTS public.song_like CASCADE;
CREATE TABLE public.song_like (
                                  user_id BIGINT NOT NULL REFERENCES public.tbl_user(id) ON DELETE CASCADE,
                                  song_id BIGINT NOT NULL REFERENCES public.song(id) ON DELETE CASCADE,
                                  PRIMARY KEY (user_id, song_id)
);

-- ============================================
-- Follow Artist
-- ============================================
DROP TABLE IF EXISTS public.follow_artist CASCADE;
CREATE TABLE public.follow_artist (
                                      user_id BIGINT NOT NULL REFERENCES public.tbl_user(id) ON DELETE CASCADE,
                                      artist_id BIGINT NOT NULL REFERENCES public.artist(id) ON DELETE CASCADE,
                                      followed_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                                      updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                                      PRIMARY KEY (user_id, artist_id)
);

-- ============================================
-- Genre Table
-- ============================================
DROP TABLE IF EXISTS public.genre CASCADE;
CREATE TABLE public.genre (
                              id BIGSERIAL PRIMARY KEY,
                              name VARCHAR(100) UNIQUE NOT NULL,
                              description TEXT

);
DROP TABLE IF EXISTS public.song_genre CASCADE;
CREATE TABLE song_genre (
                            song_id BIGINT REFERENCES song(id) ON DELETE CASCADE,
                            genre_id BIGINT REFERENCES genre(id) ON DELETE CASCADE,
                            created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                            updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                            PRIMARY KEY (song_id, genre_id)
);

-- Optional: Search Log
DROP TABLE IF EXISTS public.search_log CASCADE;
CREATE TABLE public.search_log (
                                   id BIGSERIAL PRIMARY KEY,
                                   user_id BIGINT REFERENCES public.tbl_user(id) ON DELETE SET NULL,
                                   keyword VARCHAR(255) NOT NULL,
                                   searched_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE public.country (
                                 id BIGSERIAL PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL UNIQUE,
                                 code VARCHAR(10) NOT NULL UNIQUE,
                                 created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                                 updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);