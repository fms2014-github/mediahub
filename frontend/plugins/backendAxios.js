export default function({ $axios, store }, inject) {
    const jwtToken = store.getters['login/getJwt']

    // Create a custom axios instance
    const backendAxios = $axios.create({
        headers: { Authorization: `Bearer ${jwtToken}` },
        baseURL: 'https://k02d1031.p.ssafy.io:8081/v1/',
    })

    const getMember = () => {
        return backendAxios.get('member/information')
    }
    const insertYoutubeChannel = (cId) => {
        return backendAxios.post('youtube/subscription', {
            channelId: cId,
        })
    }

    const deleteYoutubeChannel = (sId) => {
        return backendAxios.delete(`youtube/subscription/${sId}`)
    }

    const insertTwitchChannel = (params) => {
        return backendAxios.post(
            'twitch/channel/follow',
            {},
            {
                params: {
                    channelId: params.channelId,
                    accessToken: params.accessToken,
                    userId: params.userId,
                    rootLabelId: params.rootLabelId,
                },
            },
        )
    }

    const deleteTwitchChannel = (params) => {
        return backendAxios.delete('twitch/channel/follow', {
            params: {
                channelId: params.channelId,
                accessToken: params.accessToken,
                userId: params.userId,
                channelPk: params.channelPk,
            },
        })
    }

    const addFirstLogin = () => {
        return backendAxios.put('member/addFirstLogin')
    }
    const subFirstLogin = () => {
        return backendAxios.put('member/subFirstLogin')
    }

    const twitchSynchronization = () => {
        return backendAxios.put('/twitch/channel/synchronization')
    }

    const twitchTokerRefresh = () => {
        return backendAxios.get('twitch/access-token')
    }

    const backendScript = {
        getMember: () => getMember(),
        insertYoutubeChannel: (params) => insertYoutubeChannel(params),
        deleteYoutubeChannel: (sId) => deleteYoutubeChannel(sId),
        insertTwitchChannel: (params) => insertTwitchChannel(params),
        deleteTwitchChannel: (params) => deleteTwitchChannel(params),
        addFirstLogin: () => addFirstLogin(),
        subFirstLogin: () => subFirstLogin(),
        twitchSynchronization: () => twitchSynchronization(),
        twitchTokerRefresh: () => twitchTokerRefresh(),
    }

    // Inject to context as $api
    inject('backendAxios', backendScript)
}
