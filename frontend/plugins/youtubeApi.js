export default function({ $axios }, inject) {
    const accessToken =
        'ya29.a0AfH6SMDIh61B0gU8SvVou5if_r7yFUIfPtClDPWFv4fRgfzm9ZPwf-5Tw1olt_4qF4ESAfN1_nj-7OK1h44wBswrPCnUFWV4njc7Vmmkk2mF_Smsro3Drqpa3VszjTps7bcEQptwhaxLX1lHMIsSjZqcdBXSxzIagPvO'

    // Create a custom axios instance
    const youtubeApi = $axios.create({
        headers: { Authorization: `Bearer ${accessToken}` },
        baseURL: 'https://www.googleapis.com/youtube/v3/',
    })

    const apiKey = 'AIzaSyBu90FIHQnLKwEzUgeoakyc4zl_rBn7-so'

    const youtubeVideosApi = (videoId) => {
        return youtubeApi.get('videos', {
            params: {
                key: apiKey,
                part: 'snippet,liveStreamingDetails,statistics',
                id: videoId,
            },
        })
    }

    const isSubscribeApi = (channelId) => {
        return youtubeApi.get('subscriptions', {
            params: {
                part: 'snippet',
                forChannelId: channelId,
                mine: true,
            },
        })
    }

    const youtubeSearchApi = ({ channelId, eventType, type }) => {
        return youtubeApi.get('search', {
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
        return youtubeApi.get('liveChat/messages', {
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
        return youtubeApi.post(
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
        return youtubeApi.delete('subscriptions', {
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
    }
    // Inject to context as $api
    inject('youtubeApi', youtubeScript)
}
