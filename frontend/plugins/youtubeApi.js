export default function({ $axios, store }, inject) {
    const accessToken =
        'ya29.a0AfH6SMAUvarbOQhbgdC0qNACLgrvESImwdu5VTAu0SSowoQV02oWREzdRRpIZj0Sh7ZozGBfOwZ2oqfBO6gLxtcRNrp7KJbaYQNLgPFZAPbewKNwRRK_2svjtfW8VaBcyEzmdaOCBpQV4g8j2WK9Ix9HnhiGvtlu994'
    //
    const jwtToken = store.getters['login/getJwt']
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
    const apiKey = 'AIzaSyAl4t4yoO9z-WfXWC_jX6hz8SeV_7Zqjbg'

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
    const youtubeliveChatInsertApi = ({ liveChatId, msg }) => {
        return youtubeApiToken.post(
            'liveChat/messages',
            {
                snippet: {
                    liveChatId,
                    type: 'textMessageEvent',
                    textMessageDetails: {
                        messageText: msg,
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
        youtubeliveChatInsertApi: ({ liveChatId, msg }) => youtubeliveChatInsertApi({ liveChatId, msg }),
    }
    // Inject to context as $api
    inject('youtubeApi', youtubeScript)
}
