<script>
import { getCurrentUser } from './api/auth';
import { getRoleHomePath } from './utils/roleHome';
import { getToken, getUserInfo, setUserInfo } from './utils/storage';

export default {
  onLaunch() {},
  onShow() {
    this.redirectSalesAwayFromCustomerHome();
  },
  methods: {
    async redirectSalesAwayFromCustomerHome() {
      if (!getToken()) {
        return;
      }
      const pages = getCurrentPages();
      const current = pages[pages.length - 1];
      if (!current || current.route !== 'pages/home/home') {
        return;
      }
      try {
        const currentUser = await getCurrentUser();
        const nextUser = {
          ...(getUserInfo() || {}),
          ...currentUser
        };
        setUserInfo(nextUser);
        if (nextUser.roleCode === 'SALES') {
          uni.reLaunch({
            url: getRoleHomePath(nextUser)
          });
        }
      } catch {
        // Request interceptor handles invalid login state.
      }
    }
  }
};
</script>

<style>
page {
  min-height: 100%;
  background: #f6f3ec;
  color: #1f2933;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
}

view,
text,
input,
button {
  box-sizing: border-box;
}

button {
  margin: 0;
}
</style>
