import { Button } from '@nextui-org/react';
import React, { createContext, useState } from 'react';

import LoginModal from '@/login/primary/LoginModal';
import { Navigate } from 'react-router-dom';

export const UserInfoContext = createContext<UserInfoContextType | Record<string, never>>({});

const LoginForm = () => {
  const [isOpen, setOpen] = useState(false);
  const [username, setUsername] = useState<any>('');
  const [token, setToken] = useState<any>('');

  const userInfoContextValues = {
    setUsername,
    setToken,
  };

  const onClickLoginButton = () => {
    setOpen(true);
  };

  const onCloseModal = () => {
    setOpen(false);
  };

  return (
    <div>
      <Button auto shadow onClick={onClickLoginButton}>
        Se connecter
      </Button>
      {token && <Navigate to="/about" replace={true} />}
      <UserInfoContext.Provider value={userInfoContextValues}>
        <LoginModal open={isOpen} onClose={onCloseModal} />
      </UserInfoContext.Provider>
    </div>
  );
};

export default LoginForm;
