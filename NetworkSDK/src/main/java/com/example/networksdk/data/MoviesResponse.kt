package com.example.networksdk.data

data class MoviesResponse(
        val dates: Dates,
        val page: Int,
        val results: List<Movie>
) {
    override fun toString(): String {
        return "LatestMoviesResponse(dates=$dates, page=$page, results=$results)"
    }
}

data class Dates(
        val maximum: String,
        val minimum: String
) {
    override fun toString(): String {
        return "Dates(maximum='$maximum', minimum='$minimum')"
    }
}