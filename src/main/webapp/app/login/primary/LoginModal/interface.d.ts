type LoginModalType = {
  open: boolean;
  onClose: () => void;
};

type LoginModalDataType = {
  username: string;
  password: string;
  rememberMe: boolean;
};

type LoginFunctionType = {
  username: string;
  password: string;
  rememberMe: boolean;
  setUsername: (username: string) => void;
  setToken: (token: string) => void;
};

type UserInfoContextType = {
  setUsername: (username: string) => void;
  setToken: (token: string) => void;
};
