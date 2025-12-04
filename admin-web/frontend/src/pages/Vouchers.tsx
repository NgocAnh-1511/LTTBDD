import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
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
  IconButton,
  Tooltip,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  FormControlLabel,
  Switch,
  Snackbar,
  Chip,
} from '@mui/material'
import RefreshIcon from '@mui/icons-material/Refresh'
import AddIcon from '@mui/icons-material/Add'
import EditIcon from '@mui/icons-material/Edit'
import DeleteIcon from '@mui/icons-material/Delete'
import api from '../services/api'

interface Voucher {
  id?: string
  code: string
  name: string
  description?: string
  type?: 'PERCENT' | 'FIXED'
  value: number
  minPurchaseAmount?: number
  maxDiscountAmount?: number
  usageLimit?: number
  startDate?: string
  endDate?: string
  isActive?: boolean
}

export default function Vouchers() {
  const [openDialog, setOpenDialog] = useState(false)
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false)
  const [selectedVoucher, setSelectedVoucher] = useState<Voucher | null>(null)
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' })
  const queryClient = useQueryClient()

  const { data: vouchers, isLoading, error, refetch, isRefetching } = useQuery({
    queryKey: ['vouchers'],
    queryFn: () => api.get('/vouchers').then((res) => res.data),
    staleTime: 0,
    refetchOnWindowFocus: true,
  })

  const createMutation = useMutation({
    mutationFn: (data: Partial<Voucher>) => api.post('/vouchers', data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vouchers'] })
      setOpenDialog(false)
      setSnackbar({ open: true, message: 'Voucher created successfully', severity: 'success' })
    },
    onError: () => {
      setSnackbar({ open: true, message: 'Failed to create voucher', severity: 'error' })
    },
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<Voucher> }) =>
      api.patch(`/vouchers/${id}`, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vouchers'] })
      setOpenDialog(false)
      setSelectedVoucher(null)
      setSnackbar({ open: true, message: 'Voucher updated successfully', severity: 'success' })
    },
    onError: () => {
      setSnackbar({ open: true, message: 'Failed to update voucher', severity: 'error' })
    },
  })

  const deleteMutation = useMutation({
    mutationFn: (id: string) => api.delete(`/vouchers/${id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vouchers'] })
      setOpenDeleteDialog(false)
      setSelectedVoucher(null)
      setSnackbar({ open: true, message: 'Voucher deleted successfully', severity: 'success' })
    },
    onError: () => {
      setSnackbar({ open: true, message: 'Failed to delete voucher', severity: 'error' })
    },
  })

  const handleOpenAdd = () => {
    setSelectedVoucher({
      code: '',
      name: '',
      description: '',
      type: 'PERCENT',
      value: 0,
      minPurchaseAmount: 0,
      maxDiscountAmount: 0,
      usageLimit: 0,
      isActive: true,
    })
    setOpenDialog(true)
  }

  const handleOpenEdit = (voucher: any) => {
    const voucherId = voucher.voucherId || voucher.voucher_id || voucher.id
    setSelectedVoucher({
      id: voucherId,
      code: voucher.code || '',
      name: voucher.name || '',
      description: voucher.description || '',
      type: voucher.type || voucher.discountType || voucher.discount_type || 'PERCENT',
      value: voucher.value || voucher.discountPercent || voucher.discount_percent || voucher.discountAmount || voucher.discount_amount || 0,
      minPurchaseAmount: voucher.minPurchaseAmount || voucher.min_order_amount || voucher.minOrderAmount || 0,
      maxDiscountAmount: voucher.maxDiscountAmount || voucher.max_discount_amount || 0,
      usageLimit: voucher.usageLimit || voucher.usage_limit || 0,
      startDate: voucher.startDate || voucher.start_date ? new Date(voucher.startDate || voucher.start_date).toISOString().split('T')[0] : undefined,
      endDate: voucher.endDate || voucher.end_date ? new Date(voucher.endDate || voucher.end_date).toISOString().split('T')[0] : undefined,
      isActive: voucher.isActive !== undefined ? voucher.isActive : (voucher.is_active !== undefined ? voucher.is_active : true),
    })
    setOpenDialog(true)
  }

  const handleOpenDelete = (voucher: any) => {
    const voucherId = voucher.voucherId || voucher.voucher_id || voucher.id
    setSelectedVoucher({
      id: voucherId,
      code: voucher.code || '',
      name: voucher.name || '',
    })
    setOpenDeleteDialog(true)
  }

  const handleSave = () => {
    if (!selectedVoucher) return

    const data: any = {
      code: selectedVoucher.code,
      name: selectedVoucher.name,
      description: selectedVoucher.description,
      type: selectedVoucher.type,
      value: Number(selectedVoucher.value),
      minPurchaseAmount: selectedVoucher.minPurchaseAmount ? Number(selectedVoucher.minPurchaseAmount) : undefined,
      maxDiscountAmount: selectedVoucher.maxDiscountAmount ? Number(selectedVoucher.maxDiscountAmount) : undefined,
      usageLimit: selectedVoucher.usageLimit ? Number(selectedVoucher.usageLimit) : undefined,
      isActive: selectedVoucher.isActive,
    }

    if (selectedVoucher.startDate) {
      data.startDate = new Date(selectedVoucher.startDate).toISOString()
    }
    if (selectedVoucher.endDate) {
      data.endDate = new Date(selectedVoucher.endDate).toISOString()
    }

    if (selectedVoucher.id) {
      updateMutation.mutate({ id: selectedVoucher.id, data })
    } else {
      createMutation.mutate(data)
    }
  }

  const handleDelete = () => {
    if (selectedVoucher?.id) {
      deleteMutation.mutate(selectedVoucher.id)
    }
  }

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
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">
          Vouchers Management
        </Typography>
        <Box>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={handleOpenAdd}
            sx={{ mr: 1 }}
          >
            Add Voucher
          </Button>
          <Tooltip title="Refresh vouchers">
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
              <TableCell>Code</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>Value</TableCell>
              <TableCell>Min Order</TableCell>
              <TableCell>Used</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {!vouchers || vouchers.length === 0 ? (
              <TableRow>
                <TableCell colSpan={8} align="center" sx={{ py: 4 }}>
                  <Typography color="text.secondary">
                    No vouchers found
                  </Typography>
                </TableCell>
              </TableRow>
            ) : (
              vouchers.map((voucher: any) => {
                const voucherId = voucher.voucherId || voucher.voucher_id || voucher.id || '-'
                const code = voucher.code || '-'
                const name = voucher.name || '-'
                const discountType = voucher.type || voucher.discountType || voucher.discount_type || 'PERCENT'
                const discountPercent = voucher.discountPercent || voucher.discount_percent || 0
                const discountAmount = voucher.discountAmount || voucher.discount_amount || 0
                const value = voucher.value || (discountType === 'PERCENT' ? discountPercent : discountAmount)
                const minOrderAmount = voucher.minPurchaseAmount || voucher.min_order_amount || voucher.minOrderAmount || 0
                const usedCount = voucher.usedCount || voucher.used_count || 0
                const usageLimit = voucher.usageLimit || voucher.usage_limit || 0
                const isActive = voucher.isActive !== undefined ? voucher.isActive : (voucher.is_active !== undefined ? voucher.is_active : true)
                
                return (
                  <TableRow key={voucherId}>
                    <TableCell>{code}</TableCell>
                    <TableCell>{name}</TableCell>
                    <TableCell>{discountType}</TableCell>
                    <TableCell>
                      {discountType === 'PERCENT'
                        ? `${value}%`
                        : `${typeof value === 'number' ? value.toLocaleString('vi-VN') : value}đ`}
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
                    <TableCell>
                      <IconButton
                        size="small"
                        color="primary"
                        onClick={() => handleOpenEdit(voucher)}
                      >
                        <EditIcon />
                      </IconButton>
                      <IconButton
                        size="small"
                        color="error"
                        onClick={() => handleOpenDelete(voucher)}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                )
              })
            )}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Add/Edit Dialog */}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="md" fullWidth>
        <DialogTitle sx={{ fontWeight: 600 }}>
          {selectedVoucher?.id ? 'Edit Voucher' : 'Add Voucher'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 2 }}>
            <TextField
              label="Code"
              value={selectedVoucher?.code || ''}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, code: e.target.value })}
              fullWidth
              required
            />
            <TextField
              label="Name"
              value={selectedVoucher?.name || ''}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, name: e.target.value })}
              fullWidth
              required
            />
            <TextField
              label="Description"
              value={selectedVoucher?.description || ''}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, description: e.target.value })}
              fullWidth
              multiline
              rows={2}
            />
            <TextField
              select
              label="Type"
              value={selectedVoucher?.type || 'PERCENT'}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, type: e.target.value as 'PERCENT' | 'FIXED' })}
              fullWidth
            >
              <MenuItem value="PERCENT">Percent</MenuItem>
              <MenuItem value="FIXED">Fixed Amount</MenuItem>
            </TextField>
            <TextField
              label={selectedVoucher?.type === 'PERCENT' ? 'Discount Percent (%)' : 'Discount Amount'}
              type="number"
              value={selectedVoucher?.value || 0}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, value: Number(e.target.value) })}
              fullWidth
              required
            />
            <TextField
              label="Min Purchase Amount"
              type="number"
              value={selectedVoucher?.minPurchaseAmount || 0}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, minPurchaseAmount: Number(e.target.value) })}
              fullWidth
            />
            <TextField
              label="Max Discount Amount"
              type="number"
              value={selectedVoucher?.maxDiscountAmount || 0}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, maxDiscountAmount: Number(e.target.value) })}
              fullWidth
            />
            <TextField
              label="Usage Limit"
              type="number"
              value={selectedVoucher?.usageLimit || 0}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, usageLimit: Number(e.target.value) })}
              fullWidth
            />
            <TextField
              label="Start Date"
              type="date"
              value={selectedVoucher?.startDate || ''}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, startDate: e.target.value })}
              fullWidth
              InputLabelProps={{ shrink: true }}
            />
            <TextField
              label="End Date"
              type="date"
              value={selectedVoucher?.endDate || ''}
              onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, endDate: e.target.value })}
              fullWidth
              InputLabelProps={{ shrink: true }}
            />
            <FormControlLabel
              control={
                <Switch
                  checked={selectedVoucher?.isActive !== false}
                  onChange={(e) => setSelectedVoucher({ ...selectedVoucher!, isActive: e.target.checked })}
                />
              }
              label="Active"
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button
            onClick={handleSave}
            variant="contained"
            disabled={createMutation.isPending || updateMutation.isPending || !selectedVoucher?.code || !selectedVoucher?.name}
          >
            {createMutation.isPending || updateMutation.isPending ? 'Saving...' : 'Save'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirmation Dialog */}
      <Dialog open={openDeleteDialog} onClose={() => setOpenDeleteDialog(false)}>
        <DialogTitle sx={{ fontWeight: 600 }}>Confirm Delete</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete voucher "{selectedVoucher?.code}"? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDeleteDialog(false)}>Cancel</Button>
          <Button
            onClick={handleDelete}
            variant="contained"
            color="error"
            disabled={deleteMutation.isPending}
          >
            {deleteMutation.isPending ? 'Deleting...' : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Snackbar */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={3000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert severity={snackbar.severity} onClose={() => setSnackbar({ ...snackbar, open: false })}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  )
}
