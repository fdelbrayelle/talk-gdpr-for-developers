import axios from 'axios';
import { setLocalStorage } from '@/common/Services/storage';

const CUSTOMER_URL = 'http://localhost:8080/api/users';

export const customer = (customerData: any, setSuccess: any) => {
  return axios
    .post(CUSTOMER_URL, { ...customerData })
    .then(response => {
      setSuccess(true);
    })
    .catch(error => {
      setSuccess(error);
    });
};
