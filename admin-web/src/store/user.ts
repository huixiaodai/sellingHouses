import { defineStore } from 'pinia';
import { currentUserApi, loginApi, logoutApi } from '@/api/auth';
import { getToken, removeToken, setToken } from '@/utils/token';
import type { LoginRequest, UserInfo } from '@/types/auth';

interface UserState {
  token: string;
  userInfo: UserInfo | null;
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken(),
    userInfo: null
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.token)
  },
  actions: {
    async login(payload: LoginRequest) {
      const loginResult = await loginApi(payload);
      this.token = loginResult.token;
      setToken(loginResult.token);
      await this.getCurrentUser();
    },
    async getCurrentUser() {
      const userInfo = await currentUserApi();
      this.userInfo = userInfo;
      return userInfo;
    },
    async logout() {
      try {
        if (this.token) {
          await logoutApi();
        }
      } finally {
        this.clearLoginState();
      }
    },
    clearLoginState() {
      this.token = '';
      this.userInfo = null;
      removeToken();
    }
  }
});
