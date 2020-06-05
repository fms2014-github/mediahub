export default function({ $axios, store }, inject) {
    const accessToken =
        'ya29.a0AfH6SMD1sSvOJaY546r1vPJCpO1Ddk9Qiu9XH6_quegYCvVMTVUmi50bBb2XIk1yiIR7NF9OGknnZuJOS34wPBW3iSjMpScB8yXHFrweRYRNr6plD8zuRxVjM7RhVfLqnZAF12181Y30UAUpY5Uv09urD9WBIv0Mpfxj'

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
        return backendAxios.delete('youtube/subscription', {
            params: {
                subscribeId: sId,
            },
        })
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

    const backendScript = {
        getMember: () => getMember(),
        insertYoutubeChannel: (params) => insertYoutubeChannel(params),
        deleteYoutubeChannel: (sId) => deleteYoutubeChannel(sId),
        insertTwitchChannel: (params) => insertTwitchChannel(params),
        deleteTwitchChannel: (params) => deleteTwitchChannel(params),
    }

    // Inject to context as $api
    inject('backendAxios', backendScript)
}
