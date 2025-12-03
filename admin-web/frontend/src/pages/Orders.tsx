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
  IconButton,
  Tooltip,
} from '@mui/material'
import RefreshIcon from '@mui/icons-material/Refresh'
import { format } from 'date-fns'
import api from '../services/api'

const getStatusColor = (status: string) => {
  switch (status) {
    case 'Completed':
      return 'success'
    case 'Processing':
      return 'info'
    case 'Pending':
      return 'warning'
    case 'Cancelled':
      return 'error'
    default:
      return 'default'
  }
}

export default function Orders() {
  const { data: orders, isLoading, error, refetch, isRefetching } = useQuery({
    queryKey: ['orders'],
    queryFn: () => api.get('/orders').then((res) => res.data),
    staleTime: 0, // Data is immediately stale, will refetch on mount
    refetchOnWindowFocus: true, // Refetch when window gains focus
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
        Error loading orders
      </Alert>
    )
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">
          Orders Management
        </Typography>
        <Tooltip title="Refresh orders">
          <span>
            <IconButton 
              onClick={() => refetch()} 
              disabled={isRefetching}
              color="primary"
            >
              <RefreshIcon />
            </IconButton>
          </span>
        </Tooltip>
      </Box>
      {(isLoading || isRefetching) && (
        <Box sx={{ display: 'flex', justifyContent: 'center', p: 2 }}>
          <CircularProgress size={24} />
        </Box>
      )}
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Order ID</TableCell>
              <TableCell>Customer</TableCell>
              <TableCell>Total</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Date</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {!orders || orders.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} align="center" sx={{ py: 4 }}>
                  <Typography color="text.secondary">
                    No orders found
                  </Typography>
                </TableCell>
              </TableRow>
            ) : (
              orders.map((order: any) => {
                const orderId = order.orderId || order.order_id || '-'
                const customerName = order.customerName || order.customer_name || '-'
                const totalPrice = order.totalPrice || order.total_price || 0
                const status = order.status || '-'
                const orderDate = order.orderDate || order.order_date
                
                let formattedDate = '-'
                if (orderDate) {
                  try {
                    const dateValue = typeof orderDate === 'number' ? orderDate : parseInt(String(orderDate))
                    if (!isNaN(dateValue)) {
                      formattedDate = format(new Date(dateValue), 'dd/MM/yyyy HH:mm')
                    }
                  } catch (e) {
                    formattedDate = '-'
                  }
                }
                
                return (
                  <TableRow key={orderId}>
                    <TableCell>
                      {typeof orderId === 'string' ? orderId.substring(0, 8) + '...' : String(orderId)}
                    </TableCell>
                    <TableCell>{customerName}</TableCell>
                    <TableCell>
                      {typeof totalPrice === 'number' ? totalPrice.toLocaleString('vi-VN') + 'đ' : String(totalPrice)}
                    </TableCell>
                    <TableCell>
                      <Chip
                        label={status}
                        color={getStatusColor(status) as any}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>{formattedDate}</TableCell>
                  </TableRow>
                )
              })
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  )
}

