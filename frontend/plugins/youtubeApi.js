export default function({ $axios, store }, inject) {
    const timeInMs = Date.now()
    // const accessToken =
    //     'ya29.a0AfH6SMAUvarbOQhbgdC0qNACLgrvESImwdu5VTAu0SSowoQV02oWREzdRRpIZj0Sh7ZozGBfOwZ2oqfBO6gLxtcRNrp7KJbaYQNLgPFZAPbewKNwRRK_2svjtfW8VaBcyEzmdaOCBpQV4g8j2WK9Ix9HnhiGvtlu994'
    // //
    const jwtToken = store.getters['login/getJwt']
    // Create a custom axios instance
    const backendAxios = $axios.create({
        headers: { Authorization: `Bearer ${jwtToken}` },
        baseURL: 'https://k02d1031.p.ssafy.io:8081/v1/youtube',
    })

    const youtubeApiKey = $axios.create({
        baseURL: 'https://www.googleapis.com/youtube/v3/',
    })
    // const youtubeApiToken = $axios.create({
    //     headers: { Authorization: `Bearer ${accessToken}` },
    //     baseURL: 'https://www.googleapis.com/youtube/v3/',
    // })
    const apiKey = [
        // 'AIzaSyAl4t4yoO9z-WfXWC_jX6hz8SeV_7Zqjbg',
        // 'AIzaSyBZcWZTdEQjVlIqx_V_M86bke37lDvV6j8',
        'AIzaSyAeFj5orE1ldMI0P_J7LjhEKwwqrbIilmE',
        // 'AIzaSyBu90FIHQnLKwEzUgeoakyc4zl_rBn7-so',
        // 'AIzaSyCZ_rUOzHmL55FEVXwz1RjeGl4ps--mNkw',
        // 'AIzaSyDYOg3oe_oZZ8hhm3Hj7dfLUTqc6fh8QMc',
        // 'AIzaSyBo9Us9ScAWvLlhcGSssKvtst0E16lDgXs',
        // 'AIzaSyAlCTC6h-4BJPg7a--v2qL7pFeRR_OPDfc',
        // 'AIzaSyBLdqVb-uUmLn7V0b04OjR7RlKsZbFel8c',
        // 'AIzaSyBpYWkhJbea6ATLXbF_EDRR1Cig5zhg8Zg',
        // 'AIzaSyCeARSMm_RcQDEwN8oJcS5WWVi-4Hd8ku8',
        // 'AIzaSyB1QU_FPcTeZnXV3QtzZj5bE4qhVUza98Q',
        // 'AIzaSyCbdrUK6qc6PigMBDTHXc0--GpfGUJNOME',
        // 'AIzaSyBTgmHOxDaWurmnbwZfXS1r5AIqFvikuLc',
        // 'AIzaSyBHrgowLrb9pikeBRKqRxkeZtM8P5lwjrQ',
        // 'AIzaSyD6L7D9vmQezoiVsmARGFvRSUsrbNn07Qs',
        // 'AIzaSyAc6ZuKkihmURSjcfeL8PcNB6tRwIzsn8s',
        // 'AIzaSyC9g5hODYWetX520NyaRjanPhd9zApEDa0',
        // 'AIzaSyBY7P8ZXDfN8frwhLKTiIKjVZoWpW_Uurs',
        // 'AIzaSyBAdT1WQr70-KNMTaAJOATMj12056mgp9U',
        // 'AIzaSyAmly9lALRrAkt32qkdV9-VggJ6nz-kfZA',
        // 'AIzaSyDmtx5-F_JbRWoRz6WsrASdjfSOBkMDcmw',
        // 'AIzaSyC0ZzFqPbQRQQ8dhq4oOAQAeKJ47ZRyV0E',
        // 'AIzaSyCFYU8aHseyqBrKxmvRG5jiqCoJStfuukw',
        // 'AIzaSyBOYRUZK713uw5jdG2cx4beUPz0hxtTwQQ',
        'AIzaSyCS7W3j1NSf_K2CcGybSEYJwlab16f5utE',
    ]

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
                    key: apiKey[timeInMs % apiKey.length],
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
                key: apiKey[timeInMs % apiKey.length],
                part: 'snippet,liveStreamingDetails,statistics',
                id: videoId,
            },
        })
    }

    const youtubeSearchApi = ({ channelId, eventType, type }) => {
        return youtubeApiKey.get('search', {
            params: {
                key: apiKey[timeInMs % apiKey.length],
                part: 'id,snippet',
                channelId,
                eventType,
                type,
            },
        })
    }

    const youtubeSearchVideoApi = ({ channelId, pageToken = ' ', order, maxResults = 4 }) => {
        console.log('maxResults', maxResults)
        console.log('channelId', channelId)
        console.log('pageToken', pageToken)
        return youtubeApiKey.get('search', {
            params: {
                key: apiKey[timeInMs % apiKey.length],
                part: 'snippet',
                channelId,
                pageToken,
                maxResults,
                order,
                type: 'video',
            },
        })
    }

    const youtubeChannelApi = (channelId) => {
        return youtubeApiKey.get('channels', {
            params: {
                key: apiKey[timeInMs % apiKey.length],
                part: 'snippet,statistics,brandingSettings',
                id: channelId,
            },
        })
    }

    const youtubeliveChatApi = ({ liveChatId, pageToken, pollingIntervalMillis }) => {
        return youtubeApiKey.get('liveChat/messages', {
            params: {
                key: apiKey[timeInMs % apiKey.length],
                part: 'id,snippet,authorDetails',
                liveChatId,
                pageToken,
                pollingIntervalMillis,
            },
        })
    }
    const youtubeliveChatInsertApi = ({ liveChatId, msg }) => {
        return backendAxios.post('/chating', {
            liveChatId,
            messageText: msg,
        })
    }

    // const insertSubscribeApi = (cId, accessToken) => {
    //     return youtubeApiToken.post(
    //         'subscriptions',
    //         {
    //             snippet: {
    //                 resourceId: {
    //                     kind: 'youtube#channel',
    //                     channelId: cId,
    //                 },
    //             },
    //         },
    //         {
    //             headers: { Authorization: `Bearer ${accessToken}` },
    //             params: {
    //                 part: 'snippet',
    //             },
    //         },
    //     )
    // }
    // const deleteSubscribeApi = (channelId, accessToken) => {
    //     return youtubeApiToken.delete('subscriptions', {
    //         headers: { Authorization: `Bearer ${accessToken}` },
    //         params: {
    //             id: channelId,
    //         },
    //     })
    // }

    // const isSubscribeApi = (channelId, accessToken) => {
    //     return youtubeApiToken.get('subscriptions', {
    //         headers: { Authorization: `Bearer ${accessToken}` },
    //         params: {
    //             part: 'snippet',
    //             forChannelId: channelId,
    //             mine: true,
    //         },
    //     })
    // }

    const synchronization = () => {
        return backendAxios.get('/synchronization')
    }
    const youtubeScript = {
        youtubeVideosApi: (videoId) => youtubeVideosApi(videoId),
        youtubeSearchApi: ({ channelId, eventType, type }) => youtubeSearchApi({ channelId, eventType, type }),
        youtubeSearchVideoApi: ({ channelId, pageToken, order, maxResults }) => youtubeSearchVideoApi({ channelId, pageToken, order, maxResults }),
        youtubeliveChatApi: (liveChatId) => youtubeliveChatApi(liveChatId),
        // isSubscribeApi: (channelId) => isSubscribeApi(channelId),
        // insertSubscribeApi: (cId) => insertSubscribeApi(cId),
        // deleteSubscribeApi: (channelId) => deleteSubscribeApi(channelId),
        youtubuLiveVideoApi: (channel, channelName) => youtubuLiveVideoApi(channel, channelName),
        synchronization: () => synchronization(),
        youtubeliveChatInsertApi: ({ liveChatId, msg }) => youtubeliveChatInsertApi({ liveChatId, msg }),
        youtubeChannelApi: (channelId) => youtubeChannelApi(channelId),
    }
    // Inject to context as $api
    inject('youtubeApi', youtubeScript)
}
