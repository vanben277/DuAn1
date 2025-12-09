import axiosClient from '../utils/axiosClient';

export const login = (data: any) => {
  return axiosClient.post('/auth/login', data);
};

export const logout = () => {
  const refreshToken = localStorage.getItem('refreshToken');
  return axiosClient.post('/auth/logout', { refreshToken });
};