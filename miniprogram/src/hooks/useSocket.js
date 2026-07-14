import { onUnmounted, ref } from 'vue';

export function useSocket({ onRoomStatusChange } = {}) {
  const connected = ref(false);
  let socketTask = null;

  function connectSocket() {
    connected.value = false;
    return null;
  }

  function disconnectSocket() {
    if (socketTask) {
      socketTask.close();
      socketTask = null;
    }
    connected.value = false;
  }

  function updateRoomStatus(roomId, status) {
    if (typeof onRoomStatusChange === 'function') {
      onRoomStatusChange({ roomId, status });
    }
  }

  onUnmounted(disconnectSocket);

  return {
    connected,
    connectSocket,
    disconnectSocket,
    updateRoomStatus
  };
}
