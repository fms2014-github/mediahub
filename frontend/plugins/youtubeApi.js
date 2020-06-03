export default function({ $axios }, inject) {
    const accessToken =
        'ya29.a0AfH6SMAQ02v2WQ0B1ro8XNfTOJLHmRelknX36ELXuIOWJwkbxWczn9ma0v4bOSCUv0BDKJypctLuWZ5MfxCihg63pDtP_VIu1ssN7cTHQQ4Sp1Vkj60qd10Wl_p-I3npiHmC61rJULyA9tJglb-b-zgVz6k0sjrGEsIf'

    const jwtToken =
        'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwicm9sZSI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE1OTExNTkxMTAsImV4cCI6MTU5MTE2MjcxMH0.jDlpQkouncU3JDAaw9WLrJ4ZYe00p_2f2_oOvUoXHICvoVifMxhUW3nVI9Ebn8gR_z5hHcGwMEzSg22fU06iOQ'
    // Create a custom axios instance
    const backendAxios = $axios.create({
        headers: { Authorization: `Bearer ${jwtToken}` },
        baseURL: 'http://k02d1031.p.ssafy.io:8081/',
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
        return backendAxios.post(
            '/v1/member/channel',
            {
                channelId: params.channelId,
                displayName: params.name,
                name: params.name,
                provider: 'youtube',
            },
            {
                params: {
                    labelId: '3',
                },
            },
        )
    }

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
