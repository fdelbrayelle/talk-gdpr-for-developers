import axios from 'axios';

const CUSTOMER_URL = 'http://localhost:8080/api/users';

export const customer = (customerData: any, setSuccess: any) => {
  return axios
    .post(CUSTOMER_URL, { ...customerData, sex: 'MALE' })
    .then(response => {
      setSuccess(true);
    })
    .catch(error => {
      setSuccess(error);
    });
};
