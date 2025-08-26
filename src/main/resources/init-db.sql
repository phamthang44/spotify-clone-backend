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
                             artist_id BIGINT NOT NULL REFERENCES public.artist(id) ON DELETE CASCADE,
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
DROP TABLE IF EXISTS public.country CASCADE;
CREATE TABLE public.country (
                                 id BIGSERIAL PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL UNIQUE,
                                 code VARCHAR(10) NOT NULL UNIQUE,
                                 created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                                 updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

DROP TABLE IF EXISTS public.library CASCADE;
CREATE TABLE public.library (
                                 user_id BIGINT NOT NULL REFERENCES public.tbl_user(id) ON DELETE CASCADE,
                                 song_id BIGINT NOT NULL REFERENCES public.song(id) ON DELETE CASCADE,
                                 added_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                                 updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                                 favorite BOOLEAN DEFAULT TRUE,
                                 PRIMARY KEY (user_id, song_id)
);

INSERT INTO public.country (name, code) VALUES
                                            ('United States', 'US'),
                                            ('United Kingdom', 'UK'),
                                            ('Vietnam', 'VN'),
                                            ('South Korea', 'KR'),
                                            ('Japan', 'JP'),
                                            ('Canada', 'CA'),
                                            ('Australia', 'AU'),
                                            ('France', 'FR'),
                                            ('Germany', 'DE'),
                                            ('Brazil', 'BR');
INSERT INTO public.artist (name, bio, avatar_url, country_id, debut_year) VALUES
                                                                              ('Taylor Swift', 'Pop singer-songwriter from USA', 'taylor.jpg', 1, 2006),
                                                                              ('BTS', 'K-pop boy band', 'bts.jpg', 4, 2013),
                                                                              ('Ed Sheeran', 'British singer-songwriter', 'ed.jpg', 2, 2011),
                                                                              ('Ariana Grande', 'American pop star', 'ariana.jpg', 1, 2008),
                                                                              ('Sơn Tùng M-TP', 'Vietnamese pop singer', 'sontung.jpg', 3, 2012),
                                                                              ('BLACKPINK', 'K-pop girl group', 'blackpink.jpg', 4, 2016),
                                                                              ('Drake', 'Canadian rapper and singer', 'drake.jpg', 6, 2006),
                                                                              ('Justin Bieber', 'Canadian pop star', 'jb.jpg', 6, 2009),
                                                                              ('Adele', 'British singer', 'adele.jpg', 2, 2008),
                                                                              ('IU', 'Korean solo singer', 'iu.jpg', 4, 2008),
                                                                              ('The Weeknd', 'Canadian singer', 'weeknd.jpg', 6, 2010),
                                                                              ('Maroon 5', 'American pop rock band', 'maroon5.jpg', 1, 2002),
                                                                              ('Shawn Mendes', 'Canadian singer-songwriter', 'shawn.jpg', 6, 2014),
                                                                              ('Billie Eilish', 'American singer', 'billie.jpg', 1, 2015),
                                                                              ('Alan Walker', 'British-Norwegian DJ', 'alan.jpg', 2, 2015),
                                                                              ('Charlie Puth', 'American singer-songwriter', 'charlie.jpg', 1, 2015),
                                                                              ('Tóc Tiên', 'Vietnamese singer', 'toctien.jpg', 3, 2004),
                                                                              ('Noo Phước Thịnh', 'Vietnamese singer', 'noo.jpg', 3, 2009),
                                                                              ('Kygo', 'Norwegian DJ', 'kygo.jpg', 2, 2014),
                                                                              ('Zedd', 'German-Russian DJ', 'zedd.jpg', 9, 2012);

INSERT INTO public.album (title, artist_id, cover_url, release_date, total_tracks, description) VALUES
                                                                                                    ('1989', 1, '1989.jpg', '2014-10-27', 13, 'Taylor Swift album'),
                                                                                                    ('Love Yourself: Tear', 2, 'tear.jpg', '2018-05-18', 11, 'BTS album'),
                                                                                                    ('Divide', 3, 'divide.jpg', '2017-03-03', 12, 'Ed Sheeran album'),
                                                                                                    ('Sweetener', 4, 'sweetener.jpg', '2018-08-17', 15, 'Ariana Grande album'),
                                                                                                    ('Chạy Ngay Đi', 5, 'cnd.jpg', '2018-05-12', 1, 'Sơn Tùng M-TP single'),
                                                                                                    ('The Album', 6, 'thealbum.jpg', '2020-10-02', 8, 'BLACKPINK album'),
                                                                                                    ('Scorpion', 7, 'scorpion.jpg', '2018-06-29', 25, 'Drake album'),
                                                                                                    ('Purpose', 8, 'purpose.jpg', '2015-11-13', 13, 'Justin Bieber album'),
                                                                                                    ('25', 9, '25.jpg', '2015-11-20', 11, 'Adele album'),
                                                                                                    ('Palette', 10, 'palette.jpg', '2017-04-21', 10, 'IU album'),
                                                                                                    ('After Hours', 11, 'afterhours.jpg', '2020-03-20', 14, 'The Weeknd album'),
                                                                                                    ('Red Pill Blues', 12, 'rpb.jpg', '2017-11-03', 15, 'Maroon 5 album'),
                                                                                                    ('Illuminate', 13, 'illuminate.jpg', '2016-09-23', 12, 'Shawn Mendes album'),
                                                                                                    ('Happier Than Ever', 14, 'hte.jpg', '2021-07-30', 16, 'Billie Eilish album'),
                                                                                                    ('Different World', 15, 'diffworld.jpg', '2018-12-14', 15, 'Alan Walker album'),
                                                                                                    ('Voicenotes', 16, 'voicenotes.jpg', '2018-05-11', 13, 'Charlie Puth album'),
                                                                                                    ('Hoàng', 17, 'hoang.jpg', '2020-12-11', 10, 'Tóc Tiên album'),
                                                                                                    ('Chạm Khẽ Tim Anh Một Chút Thôi', 18, 'ckta.jpg', '2017-11-17', 1, 'Noo Phước Thịnh single'),
                                                                                                    ('Golden Hour', 19, 'goldenhour.jpg', '2020-05-29', 12, 'Kygo album'),
                                                                                                    ('Clarity', 20, 'clarity.jpg', '2012-10-02', 10, 'Zedd album');
INSERT INTO public.genre (name, description) VALUES
                                                 ('Pop', 'Popular contemporary music'),
                                                 ('K-pop', 'Korean pop music'),
                                                 ('Rock', 'Rock music genre'),
                                                 ('Hip Hop', 'Hip hop and rap music'),
                                                 ('R&B', 'Rhythm and Blues'),
                                                 ('EDM', 'Electronic Dance Music'),
                                                 ('Indie', 'Independent music'),
                                                 ('Jazz', 'Jazz music'),
                                                 ('Classical', 'Classical and orchestral music'),
                                                 ('Country', 'Country and western music'),
                                                 ('Folk', 'Folk and acoustic music'),
                                                 ('Ballad', 'Slow sentimental music'),
                                                 ('Reggae', 'Jamaican reggae music'),
                                                 ('Blues', 'Blues music'),
                                                 ('Metal', 'Heavy metal music'),
                                                 ('Latin', 'Latin American music'),
                                                 ('Dance', 'Dance-oriented music'),
                                                 ('House', 'House electronic music'),
                                                 ('Trap', 'Trap hip hop music'),
                                                 ('Acoustic', 'Acoustic unplugged music');

INSERT INTO public.song (title, artist_id, album_id, duration, genre_id, release_date, cover_url, audio_url, status, play_count, like_count) VALUES
                                                                                                                                                 ('Blank Space', 1, 1, 231, 1, '2014-10-27', 'blankspace.jpg', 'blankspace.mp3', 'PUBLISHED', 1000000, 500000),
                                                                                                                                                 ('DNA', 2, 2, 220, 2, '2017-09-18', 'dna.jpg', 'dna.mp3', 'PUBLISHED', 2000000, 1500000),
                                                                                                                                                 ('Shape of You', 3, 3, 263, 1, '2017-01-06', 'shapeofyou.jpg', 'shapeofyou.mp3', 'PUBLISHED', 3000000, 2500000),
                                                                                                                                                 ('No Tears Left To Cry', 4, 4, 205, 1, '2018-04-20', 'notears.jpg', 'notears.mp3', 'PUBLISHED', 1500000, 1000000),
                                                                                                                                                 ('Chạy Ngay Đi', 5, 5, 240, 1, '2018-05-12', 'cndsong.jpg', 'cndsong.mp3', 'PUBLISHED', 1200000, 900000),
                                                                                                                                                 ('How You Like That', 6, 6, 203, 2, '2020-06-26', 'hyltsong.jpg', 'hyltsong.mp3', 'PUBLISHED', 2500000, 1800000),
                                                                                                                                                 ('God''s Plan', 7, 7, 198, 4, '2018-01-19', 'godsplan.jpg', 'godsplan.mp3', 'PUBLISHED', 2700000, 2000000),
                                                                                                                                                 ('Sorry', 8, 8, 200, 1, '2015-10-23', 'sorry.jpg', 'sorry.mp3', 'PUBLISHED', 2400000, 1900000),
                                                                                                                                                 ('Hello', 9, 9, 295, 1, '2015-10-23', 'hello.jpg', 'hello.mp3', 'PUBLISHED', 3500000, 2800000),
                                                                                                                                                 ('Palette', 10, 10, 217, 2, '2017-04-21', 'palette.jpg', 'palette.mp3', 'PUBLISHED', 1800000, 1400000),
                                                                                                                                                 ('Blinding Lights', 11, 11, 200, 1, '2019-11-29', 'blindinglights.jpg', 'blindinglights.mp3', 'PUBLISHED', 4000000, 3200000),
                                                                                                                                                 ('Girls Like You', 12, 12, 235, 1, '2018-05-30', 'girlslikeyou.jpg', 'girlslikeyou.mp3', 'PUBLISHED', 2800000, 2100000),
                                                                                                                                                 ('Stitches', 13, 13, 206, 1, '2015-05-05', 'stitches.jpg', 'stitches.mp3', 'PUBLISHED', 1700000, 1300000),
                                                                                                                                                 ('Bad Guy', 14, 14, 194, 1, '2019-03-29', 'badguy.jpg', 'badguy.mp3', 'PUBLISHED', 3000000, 2500000),
                                                                                                                                                 ('Faded', 15, 15, 212, 6, '2015-12-03', 'faded.jpg', 'faded.mp3', 'PUBLISHED', 2600000, 2200000),
                                                                                                                                                 ('Attention', 16, 16, 211, 1, '2017-04-21', 'attention.jpg', 'attention.mp3', 'PUBLISHED', 1500000, 1200000),
                                                                                                                                                 ('Ngày Mai Sẽ Khác', 17, 17, 230, 1, '2020-12-11', 'ngaymai.jpg', 'ngaymai.mp3', 'PUBLISHED', 900000, 700000),
                                                                                                                                                 ('Chạm Khẽ Tim Anh Một Chút Thôi', 18, 18, 240, 1, '2017-11-17', 'ckta.jpg', 'ckta.mp3', 'PUBLISHED', 850000, 650000),
                                                                                                                                                 ('Lose Somebody', 19, 19, 202, 1, '2020-05-29', 'losesomebody.jpg', 'losesomebody.mp3', 'PUBLISHED', 1300000, 1000000),
                                                                                                                                                 ('Clarity', 20, 20, 248, 6, '2012-10-02', 'clarity.jpg', 'clarity.mp3', 'PUBLISHED', 1100000, 900000),
                                                                                                                                                 ('Love Story', 1, 1, 235, 1, '2008-09-15', 'lovestory.jpg', 'lovestory.mp3', 'PUBLISHED', 1400000, 1100000),
                                                                                                                                                 ('Boy With Luv', 2, 2, 229, 2, '2019-04-12', 'bwl.jpg', 'bwl.mp3', 'PUBLISHED', 2600000, 2100000),
                                                                                                                                                 ('Photograph', 3, 3, 258, 1, '2015-05-11', 'photograph.jpg', 'photograph.mp3', 'PUBLISHED', 1900000, 1500000),
                                                                                                                                                 ('Into You', 4, 4, 248, 1, '2016-05-06', 'intoyou.jpg', 'intoyou.mp3', 'PUBLISHED', 1700000, 1400000),
                                                                                                                                                 ('Em Của Ngày Hôm Qua', 5, 5, 245, 1, '2014-02-08', 'ecnhq.jpg', 'ecnhq.mp3', 'PUBLISHED', 1000000, 800000);




--DROP TABLE public.refresh_tokens;

CREATE TABLE public.refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL REFERENCES public.tbl_user(id) ON DELETE CASCADE,
                                token_hash VARCHAR(255) NOT NULL,
                                created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
                                expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
                                revoked BOOLEAN DEFAULT FALSE,
                                UNIQUE(user_id, token_hash)
);

CREATE INDEX idx_refresh_tokens_token ON public.refresh_tokens(token_hash);

ALTER TABLE public.tbl_user ADD COLUMN auth_provider VARCHAR(50) DEFAULT 'LOCAL';
ALTER TABLE public.tbl_user ADD COLUMN provider_id VARCHAR(255);

CREATE TABLE public.user_asset (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES public.tbl_user(id) ON DELETE SET NULL,
    asset_type VARCHAR(50) NOT NULL,
    url TEXT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

CREATE TABLE public.verification_token (
                                    id BIGSERIAL PRIMARY KEY,
                                    user_id BIGINT NOT NULL REFERENCES tbl_user(id) ON DELETE CASCADE,
                                    token VARCHAR(255) NOT NULL UNIQUE,
                                    expiry_date TIMESTAMP NOT NULL,
                                    created_at TIMESTAMP DEFAULT NOW()
);