export default function({ $axios }, inject) {
    // Create a custom axios instance
    const youtubeApi = $axios.create({
        baseURL: 'https://www.googleapis.com/youtube/v3/',
    })

    // Inject to context as $api
    inject('youtubeApi', youtubeApi)
}
