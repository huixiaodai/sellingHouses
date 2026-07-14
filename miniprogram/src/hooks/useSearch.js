import { onUnmounted, ref } from 'vue';

export function useSearch(searcher, delay = 300) {
  const keyword = ref('');
  const searching = ref(false);
  let timer = null;

  function clearTimer() {
    if (timer) {
      clearTimeout(timer);
      timer = null;
    }
  }

  function run(value = keyword.value) {
    clearTimer();
    keyword.value = value;
    return new Promise((resolve, reject) => {
      timer = setTimeout(async () => {
        const text = String(keyword.value || '').trim();
        if (!text) {
          resolve([]);
          return;
        }
        searching.value = true;
        try {
          resolve(await searcher(text));
        } catch (error) {
          reject(error);
        } finally {
          searching.value = false;
        }
      }, delay);
    });
  }

  onUnmounted(clearTimer);

  return {
    keyword,
    searching,
    run,
    clearTimer
  };
}
