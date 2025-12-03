import { useQuery } from '@tanstack/react-query'
import {
  Box,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  CircularProgress,
  Alert,
  Chip,
} from '@mui/material'
import api from '../services/api'

export default function Vouchers() {
  const { data: vouchers, isLoading, error } = useQuery({
    queryKey: ['vouchers'],
    queryFn: () => api.get('/vouchers').then((res) => res.data),
  })

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
        <CircularProgress />
      </Box>
    )
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ m: 2 }}>
        Error loading vouchers
      </Alert>
    )
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Vouchers Management
      </Typography>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Code</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>Value</TableCell>
              <TableCell>Min Order</TableCell>
              <TableCell>Used</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {vouchers?.map((voucher: any) => {
              const voucherId = voucher.voucherId || voucher.voucher_id || '-'
              const code = voucher.code || '-'
              const discountType = voucher.discountType || voucher.discount_type || 'PERCENT'
              const discountPercent = voucher.discountPercent || voucher.discount_percent || 0
              const discountAmount = voucher.discountAmount || voucher.discount_amount || 0
              const minOrderAmount = voucher.minOrderAmount || voucher.min_order_amount || 0
              const usedCount = voucher.usedCount || voucher.used_count || 0
              const usageLimit = voucher.usageLimit || voucher.usage_limit || 0
              const isActive = voucher.isActive !== undefined ? voucher.isActive : (voucher.is_active !== undefined ? voucher.is_active : true)
              
              return (
                <TableRow key={voucherId}>
                  <TableCell>{code}</TableCell>
                  <TableCell>{discountType}</TableCell>
                  <TableCell>
                    {discountType === 'PERCENT'
                      ? `${discountPercent}%`
                      : `${typeof discountAmount === 'number' ? discountAmount.toLocaleString('vi-VN') : discountAmount}đ`}
                  </TableCell>
                  <TableCell>
                    {typeof minOrderAmount === 'number' ? minOrderAmount.toLocaleString('vi-VN') + 'đ' : minOrderAmount}
                  </TableCell>
                  <TableCell>
                    {usedCount} / {usageLimit || '∞'}
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={isActive ? 'Active' : 'Inactive'}
                      color={isActive ? 'success' : 'default'}
                      size="small"
                    />
                  </TableCell>
                </TableRow>
              )
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  )
}

