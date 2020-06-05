export default function({ $axios }, inject) {
    const accessToken =
        'ya29.a0AfH6SMC6Erz11saBrKI13dNBEfHYu6-9MwSHm3wpqpCUiufYfVzPf6RNheHyUTHxCtR6E_SNzfoWW89obx5KFgGUmcprfLyAJS_Qa4IxwpCu4GDChSGhU_uzc03gLi_MxXKqbaTo8X7PyMz3CavY4UOloe2VCgXS69Xv'

    const jwtToken =
        'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwicm9sZSI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJmaXJzdExvZ2luIjowLCJpYXQiOjE1OTEyNDgyNDIsImV4cCI6MTU5MjA4NDI0Mn0.e88bwRLkvO_w_QzncRL_jMY5dWUxT3duRYB6K-AqbLCRxadO7r_q9nWjdxbughZC8yXIb0HM9fRa2y-qiZTivQ'
    // Create a custom axios instance
    const backendAxios = $axios.create({
        headers: { Authorization: `Bearer ${jwtToken}` },
        baseURL: 'https://k02d1031.p.ssafy.io:8081/',
    })

    const youtubeApiKey = $axios.create({
        baseURL: 'https://www.googleapis.com/youtube/v3/',
    })
    const youtubeApiToken = $axios.create({
        headers: { Authorization: `Bearer ${accessToken}` },
        baseURL: 'https://www.googleapis.com/youtube/v3/',
    })

    const apiKey = 'AIzaSyBY7P8ZXDfN8frwhLKTiIKjVZoWpW_Uurs'

    const youtubuLiveVideoApi = async (channel, channelName) => {
        let data = null
        await youtubeApiKey
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
        return youtubeApiKey.get('videos', {
            params: {
                key: apiKey,
                part: 'snippet,liveStreamingDetails,statistics',
                id: videoId,
            },
        })
    }

    const isSubscribeApi = (channelId) => {
        return youtubeApiToken.get('subscriptions', {
            params: {
                part: 'snippet',
                forChannelId: channelId,
                mine: true,
            },
        })
    }

    const youtubeSearchApi = ({ channelId, eventType, type }) => {
        return youtubeApiKey.get('search', {
            params: {
                key: apiKey,
                part: 'id,snippet',
                channelId,
                eventType,
                type,
            },
        })
    }

    const youtubeliveChatApi = ({ liveChatId, pageToken, pollingIntervalMillis }) => {
        console.log(liveChatId)
        const query = {
            params: {
                key: apiKey,
                part: 'id,snippet,authorDetails',
                liveChatId,
                pageToken,
                pollingIntervalMillis,
            },
        }
        console.log(query)
        return youtubeApiKey.get('liveChat/messages', {
            params: {
                key: apiKey,
                part: 'id,snippet,authorDetails',
                liveChatId,
                pageToken,
                pollingIntervalMillis,
            },
        })
    }
    const insertSubscribeApi = (cId) => {
        return youtubeApiToken.post(
            'subscriptions',
            {
                snippet: {
                    resourceId: {
                        kind: 'youtube#channel',
                        channelId: cId,
                    },
                },
            },
            {
                params: {
                    part: 'snippet',
                },
            },
        )
    }
    const deleteSubscribeApi = (channelId) => {
        return youtubeApiToken.delete('subscriptions', {
            params: {
                id: channelId,
            },
        })
    }

    const youtubeScript = {
        youtubeVideosApi: (videoId) => youtubeVideosApi(videoId),
        youtubeSearchApi: ({ channelId, eventType, type }) => youtubeSearchApi({ channelId, eventType, type }),
        youtubeliveChatApi: (liveChatId) => youtubeliveChatApi(liveChatId),
        isSubscribeApi: (channelId) => isSubscribeApi(channelId),
        insertSubscribeApi: (cId) => insertSubscribeApi(cId),
        deleteSubscribeApi: (channelId) => deleteSubscribeApi(channelId),
        youtubuLiveVideoApi: (channel, channelName) => youtubuLiveVideoApi(channel, channelName),
    }
    // Inject to context as $api
    inject('youtubeApi', youtubeScript)
}
