export default function({ $axios }, inject) {
    const accessToken =
        'ya29.a0AfH6SMAKAqSNstRE6iF1W50Nb_5iDFlwaxlycebBjF3E4biQFHcuv_qAqLcR3IAGEbUvID4U85bRJUlsjtdrP2CeUZx4f0mwJh-6yz6AJ_RG8eu2tWkn6K7PtP87gs1O61hz_1mxOBDzb_1zW-bt9yuRAHzuuqa0CA-M'

    const jwtToken =
        'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwicm9sZSI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJmaXJzdExvZ2luIjowLCJpYXQiOjE1OTEyNDgyNDIsImV4cCI6MTU5MjA4NDI0Mn0.e88bwRLkvO_w_QzncRL_jMY5dWUxT3duRYB6K-AqbLCRxadO7r_q9nWjdxbughZC8yXIb0HM9fRa2y-qiZTivQ'
    // Create a custom axios instance
    const backendAxios = $axios.create({
        headers: { Authorization: `Bearer ${jwtToken}` },
        baseURL: 'https://k02d1031.p.ssafy.io:8081/',
    })

    const youtubeApi = $axios.create({
        headers: { Authorization: `Bearer ${accessToken}` },
        baseURL: 'https://www.googleapis.com/youtube/v3/',
    })

    const isSubscribeApi = (channelId) => {
        return youtubeApi.get('subscriptions', {
            params: {
                part: 'snippet',
                forChannelId: channelId,
                mine: true,
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

    const insertChannel = (params) => {
        return backendAxios.get(`/v1/youtube/subscription/insert/${params.channelId}`)
    }

    // const insertChannel = (params) => {
    //     return backendAxios.post(
    //         `/v1/youtube/subscription/insert/${params.channelId}`,
    //         {
    //             channelId: params.channelId,
    //             provider: params.kind,
    //         },
    //         {
    //             params: {
    //                 labelId: '2',
    //             },
    //         },
    //     )
    // }

    const deleteChannel = (cId) => {
        return backendAxios.delete('/v1/member/channel', {
            params: {
                channelId: cId,
            },
        })
    }

    const backendScript = {
        insertChannel: (params) => insertChannel(params),
        deleteChannel: (cId) => deleteChannel(cId),
    }

    const youtubeScript = {
        isSubscribeApi: (channelId) => isSubscribeApi(channelId),
        insertSubscribeApi: (cId) => insertSubscribeApi(cId),
        deleteSubscribeApi: (channelId) => deleteSubscribeApi(channelId),
    }

    // Inject to context as $api
    inject('youtubeApi', youtubeScript)
    inject('backendAxios', backendScript)
}
