import axiosClient from '../utils/axiosClient';

export const getStores = () => {
  return axiosClient.get('/stores');
};