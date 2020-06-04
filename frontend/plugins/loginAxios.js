export default function({ $axios }, inject) {
    // Create a custom axios instance
    const loginAxios = $axios.create({
        baseURL: 'https://k02d1031.p.ssafy.io/',
        header: {
            // 'Access-Control-Allow-Origin': '*',
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
    })

    // Inject to context as $api
    inject('loginAxios', loginAxios)
}
