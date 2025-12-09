import axiosClient from '../utils/axiosClient';

export const getUsers = (params: any) => {
  return axiosClient.get('/users/filters', { params });
};

export const getUserDetail = (id: any) => {
  return axiosClient.get(`/users/${id}`);
};

export const registerUser = (data: any) => {
  return axiosClient.post('/users/register', data);
};

export const updateUser = (id: any, data: any) => {
  return axiosClient.put(`/users/${id}`, data);
};

export const deleteUser = (id: number) => {
  return axiosClient.delete(`/users/${id}`);
};

export const getRoles = () => {
  return axiosClient.get('/users/roles');
};