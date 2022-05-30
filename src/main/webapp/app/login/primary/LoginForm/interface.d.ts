type LoginFormType = {
  open: boolean;
  onClose: () => void;
};

type LoginFormDataType = {
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
