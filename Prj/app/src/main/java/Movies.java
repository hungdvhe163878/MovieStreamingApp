public class Movies {
    private String title;
    private String category;
    private String release_date;
    private String trailer_url;
    private String description;
    private String[] actors;
    private int likes;
    private String[] languages;
    private String[] subtitles;
    private String[] quality;

    public Movies() {
    }

    public Movies(String title, String category, String release_date, String trailer_url, String description, String[] actors, int likes, String[] languages, String[] subtitles, String[] quality) {
        this.title = title;
        this.category = category;
        this.release_date = release_date;
        this.trailer_url = trailer_url;
        this.description = description;
        this.actors = actors;
        this.likes = likes;
        this.languages = languages;
        this.subtitles = subtitles;
        this.quality = quality;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTrailer_url() {
        return trailer_url;
    }

    public void setTrailer_url(String trailer_url) {
        this.trailer_url = trailer_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public String[] getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String[] subtitles) {
        this.subtitles = subtitles;
    }

    public String[] getQuality() {
        return quality;
    }

    public void setQuality(String[] quality) {
        this.quality = quality;
    }
}
