export default function({ $axios }, inject) {
    // Create a custom axios instance
    const youtubeApi = $axios.create({
        baseURL: 'https://www.googleapis.com/youtube/v3/',
    })

    // const apiKey = 'AIzaSyCOjxLOMLfICePbWX4CJeZFQDhq6XORxmE' // 내꺼
    // const apiKey = 'AIzaSyDgJfIT5cIz7vMUYB0l2gDdNiN0mfIJkOw' // 상헌오빠꺼25705
    // const apiKey = 'AIzaSyCTdSK1LEGKXRnmusxqrnA24Q6cwQ9Utoc' // 상헌오빠꺼2880
    // const apiKey = 'AIzaSyCwWOSPc9DzQKjPkVxc9nDIEEnnRZRByts' // 상헌오빠꺼57526
    // const apiKey = 'AIzaSyCId51LperO-JYgvGqATrHdIBJMet7nP6M' // 상헌오빠꺼970
    const apiKey = 'AIzaSyAz_ZTLDo0dFTwcSUec8QTrnzNvAQ0iGt0' // 상헌오빠꺼1320
    const youtubuLiveVideoApi = async (channel, channelName) => {
        let data = null
        await youtubeApi
            .get('search', {
                params: {
                    part: 'id',
                    channelId: channel,
                    eventType: 'live',
                    q: channelName,
                    type: 'video',
                    key: apiKey,
                },
            })
            .then((res) => {
                data = res.data
            })
        return data
    }

    const youtubeVideosApi = (videoId) => {
        return youtubeApi.get('videos', {
            params: {
                key: apiKey,
                part: 'snippet,liveStreamingDetails,statistics',
                id: videoId,
            },
        })
    }
    const otherApi = (data) => {
        return youtubeApi.get('search', {
            params: {},
        })
    }
    const axiosScript = {
        youtubuLiveVideoApi: (channelId, channelName) => youtubuLiveVideoApi(channelId, channelName),
        youtubeVideosApi: (videoId) => youtubeVideosApi(videoId),
        otherApi: (data) => otherApi(data),
    }

    // Inject to context as $api
    inject('youtubeApi', axiosScript)
}
