
const image = {
    state: {
        imageRootPath: "http://localhost:8099/resources",
        avatar: '',
        logoId: -1,
    },
    mutations: {
        SET_AVATAR: (state, avatar) => {
            state.avatar = avatar
        },
        SET_LOGOID: (state, logoId) => {
            state.logoId = logoId
        },
    },
    actions: {
        setAvatar({ commit }, avatar) {
            commit('SET_AVATAR', avatar)
        },
        setLogoId({ commit }, logoId) {
            commit('SET_LOGOID', logoId)
        },
    }
}
export default image