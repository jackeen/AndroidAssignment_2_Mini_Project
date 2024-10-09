package app.fatal.androidassignment_2_mini_project.models

enum class Category(val text: String, val id: Int) {
    ANY("", 0),

    GENERAL_KNOWLEDGE("General Knowledge", 9),

    ENTERTAINMENT_BOOKS("Entertainment: Books", 10),
    ENTERTAINMENT_FILM("Entertainment: Film", 11),
    ENTERTAINMENT_MUSIC("Entertainment: Music", 12),
    ENTERTAINMENT_MUSICALS_THEATRES("Entertainment: Musicals & Theatres", 13),
    ENTERTAINMENT_TELEVISION("Entertainment: Television", 14),
    ENTERTAINMENT_VIDEO_GAME("Entertainment: Video Games", 15),
    ENTERTAINMENT_BOARD_GAME("Entertainment: Board Games", 16),
    ENTERTAINMENT_COMICS("Entertainment: Comics", 29),
    ENTERTAINMENT_JAPANESE_ANIME_MANGA("Entertainment: Japanese Anime & Manga", 31),
    ENTERTAINMENT_CARTOON_ANIMATIONS("Entertainment: Cartoon & Animations", 32),

    SCIENCE_NATURE("Science & Nature", 17),
    SCIENCE_COMPUTERS("Science: Computers", 18),
    SCIENCE_MATHEMATICS("Science: Mathematics", 19),
    SCIENCE_GADGETS("Science: Gadgets", 30),

    MYTHOLOGY("Mythology", 20),
    SPORTS("Sports", 21),
    GEOGRAPHY("Geography", 22),
    HISTORY("History", 23),
    POLITICS("Politics", 24),
    ART("Art", 25),
    CELEBRITIES("Celebrities", 26),
    ANIMALS("Animals", 27),
    VEHICLES("Vehicles", 28),
}